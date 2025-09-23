package com.vladabur.popcorn.domain.repositories

import androidx.paging.PagingData
import com.vladabur.popcorn.data.database.entities.MovieEntity
import kotlinx.coroutines.flow.Flow


interface MovieRepository {
    suspend fun getMovies(): Flow<PagingData<MovieEntity>>
}