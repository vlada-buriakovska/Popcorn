package com.vladabur.popcorn.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vladabur.popcorn.data.extensions.mapToApiErrors
import com.vladabur.popcorn.data.paging.MoviePagingSource
import com.vladabur.popcorn.data.services.MovieService
import com.vladabur.popcorn.domain.models.movie.Movie
import com.vladabur.popcorn.domain.repositories.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService
) :
    MovieRepository {
    override suspend fun getMovies(pageSize: Int): Flow<PagingData<Movie>> {
        return try {
            Pager(
                config = PagingConfig(pageSize = pageSize),
                pagingSourceFactory = {
                    MoviePagingSource(movieService)
                }
            )
                .flow
        } catch (e: Exception) {
            throw e.mapToApiErrors()
        }
    }
}