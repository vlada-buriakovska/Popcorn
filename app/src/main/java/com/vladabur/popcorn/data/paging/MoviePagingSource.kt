package com.vladabur.popcorn.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vladabur.popcorn.data.mappers.toMovie
import com.vladabur.popcorn.data.services.MovieService
import com.vladabur.popcorn.domain.models.movie.Movie
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException


class MoviePagingSource(
    private val movieService: MovieService
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            //FIXME just to show loading process
            delay(2000L)
            
            val nextPageNumber = params.key ?: 1
            val moviesResponse = movieService.getMovies(nextPageNumber)
            return LoadResult.Page(
                data = moviesResponse.results?.map { it.toMovie() } ?: listOf(),
                prevKey = null,
                nextKey = moviesResponse.page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}