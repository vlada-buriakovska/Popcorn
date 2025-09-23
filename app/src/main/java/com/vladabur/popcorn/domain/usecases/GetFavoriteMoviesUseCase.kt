package com.vladabur.popcorn.domain.usecases

import androidx.paging.PagingData
import com.vladabur.popcorn.data.database.entities.MovieEntity
import com.vladabur.popcorn.domain.repositories.MovieRepository
import com.vladabur.popcorn.domain.usecases.base.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetFavoriteMoviesUseCase @Inject constructor(private val movieRepository: MovieRepository) :
    BaseUseCase<Unit?, Flow<PagingData<MovieEntity>>>() {

    override suspend fun remoteWork(params: Unit?): Flow<PagingData<MovieEntity>> {
        return withContext(Dispatchers.IO) {
            movieRepository.getFavoriteMovies()
        }
    }
}