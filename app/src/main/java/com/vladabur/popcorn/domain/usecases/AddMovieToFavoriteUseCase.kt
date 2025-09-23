package com.vladabur.popcorn.domain.usecases

import com.vladabur.popcorn.domain.models.movie.Movie
import com.vladabur.popcorn.domain.repositories.MovieRepository
import com.vladabur.popcorn.domain.usecases.AddMovieToFavoriteUseCase.Params
import com.vladabur.popcorn.domain.usecases.base.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddMovieToFavoriteUseCase @Inject constructor(private val movieRepository: MovieRepository) :
    BaseUseCase<Params, Unit>() {
    override suspend fun remoteWork(params: Params?) {
        return withContext(Dispatchers.IO) {
            movieRepository.addMovieToFavorite(params!!.movie)
        }
    }

    class Params(
        val movie: Movie
    )

}