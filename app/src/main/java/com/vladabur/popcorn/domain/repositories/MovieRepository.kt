package com.vladabur.popcorn.domain.repositories

import androidx.paging.PagingData
import com.vladabur.popcorn.domain.models.movie.Movie
import kotlinx.coroutines.flow.Flow


interface MovieRepository {
    suspend fun getMovies(pageSize: Int): Flow<PagingData<Movie>>
}