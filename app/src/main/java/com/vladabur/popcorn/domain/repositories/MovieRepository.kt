package com.vladabur.popcorn.domain.repositories

import androidx.paging.PagingData
import com.vladabur.popcorn.data.database.entities.MovieEntity
import com.vladabur.popcorn.domain.models.movie.Movie
import kotlinx.coroutines.flow.Flow


interface MovieRepository {
    suspend fun getMovies(): Flow<PagingData<MovieEntity>>
    suspend fun getFavoriteMovies(): Flow<PagingData<MovieEntity>>
    suspend fun addMovieToFavorite(movie: Movie)
    suspend fun removeMovieFromFavorite(movie: Movie)
}