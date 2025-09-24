package com.vladabur.popcorn.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.vladabur.popcorn.data.database.entities.MovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM ${MovieEntity.ENTITY_NAME}")
    fun pagingSource(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM ${MovieEntity.ENTITY_NAME} WHERE isFavorite=1")
    fun favoritePagingSource(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity?
    
    @Update
    suspend fun updateMovie(movie: MovieEntity)
    
    @Upsert
    suspend fun upsertAll(movies: List<MovieEntity>)

    @Query("DELETE FROM ${MovieEntity.ENTITY_NAME}")
    suspend fun clearAll()
}