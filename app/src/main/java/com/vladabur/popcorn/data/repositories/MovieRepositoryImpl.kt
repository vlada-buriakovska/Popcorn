package com.vladabur.popcorn.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vladabur.popcorn.data.database.AppDatabase
import com.vladabur.popcorn.data.database.entities.MovieEntity
import com.vladabur.popcorn.data.extensions.mapToApiErrors
import com.vladabur.popcorn.data.mappers.toMovieEntity
import com.vladabur.popcorn.data.paging.MovieRemoteMediator
import com.vladabur.popcorn.data.services.MovieService
import com.vladabur.popcorn.domain.models.movie.Movie
import com.vladabur.popcorn.domain.repositories.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService,
    private val appDatabase: AppDatabase
) : MovieRepository {
    override suspend fun getMovies(): Flow<PagingData<MovieEntity>> {
        return try {
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                ),
                remoteMediator = MovieRemoteMediator(
                    movieService = movieService,
                    appDatabase = appDatabase
                )
            ) {
                appDatabase.movieDao().pagingSource()
            }
                .flow
        } catch (e: Exception) {
            throw e.mapToApiErrors()
        }
    }

    override suspend fun getFavoriteMovies(): Flow<PagingData<MovieEntity>> {
        return try {
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                )
            ) {
                appDatabase.movieDao().favoritePagingSource()
            }
                .flow
        } catch (e: Exception) {
            throw e.mapToApiErrors()
        }
    }

    override suspend fun addMovieToFavorite(movie: Movie) {
        return try {
            val movieToFavorite = movie.copy(isFavorite = true).toMovieEntity()
            appDatabase.movieDao().updateMovie(movieToFavorite)
        } catch (e: Exception) {
            throw e.mapToApiErrors()
        }
    }

    override suspend fun removeMovieFromFavorite(movie: Movie) {
        return try {
            val movieToFavorite = movie.copy(isFavorite = false).toMovieEntity()
            appDatabase.movieDao().updateMovie(movieToFavorite)
        } catch (e: Exception) {
            throw e.mapToApiErrors()
        }
    }
}