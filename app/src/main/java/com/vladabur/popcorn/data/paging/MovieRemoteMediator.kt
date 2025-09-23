package com.vladabur.popcorn.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH
import androidx.room.withTransaction
import com.vladabur.popcorn.data.database.AppDatabase
import com.vladabur.popcorn.data.database.entities.MovieEntity
import com.vladabur.popcorn.data.database.entities.RemoteKeyEntity
import com.vladabur.popcorn.data.mappers.toMovieEntity
import com.vladabur.popcorn.data.services.MovieService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val movieService: MovieService,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, MovieEntity>() {
    override suspend fun initialize(): InitializeAction {
        return SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = appDatabase.remoteKeyDao().getRemoteKey()
                    remoteKey?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }
            val moviesResponse = movieService.getMovies(page)
            val movies = moviesResponse.results
            val endOfPaginationReached = movies.isNullOrEmpty()

            appDatabase.withTransaction {
                val moviesEntities = movies?.map { movie ->
                    val entity = appDatabase.movieDao().getMovieById(movie.id)
                    movie.toMovieEntity().copy(isFavorite = entity?.isFavorite == true)
                }
                if (loadType == LoadType.REFRESH) {
                    appDatabase.movieDao().clearAll()
                    appDatabase.remoteKeyDao().clearAll()
                }
                moviesEntities?.let { appDatabase.movieDao().upsertAll(it) }
                val nextKey = if (endOfPaginationReached) null else moviesResponse.page + 1
                appDatabase.remoteKeyDao().insertOrReplace(RemoteKeyEntity(nextKey = nextKey))
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}