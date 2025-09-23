package com.vladabur.popcorn.domain.usecases

import androidx.paging.PagingData
import com.vladabur.popcorn.domain.models.movie.Movie
import com.vladabur.popcorn.domain.repositories.MovieRepository
import com.vladabur.popcorn.domain.usecases.GetMoviesUseCase.Params
import com.vladabur.popcorn.domain.usecases.base.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class GetMoviesUseCase @Inject constructor(private val movieRepository: MovieRepository) :
    BaseUseCase<Params, Flow<PagingData<Movie>>>() {

    override suspend fun remoteWork(params: Params?): Flow<PagingData<Movie>> {
        return withContext(Dispatchers.IO) {
            movieRepository.getMovies(params!!.pageSize)
        }
    }

    class Params(
        val pageSize: Int
    )

}