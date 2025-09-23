package com.vladabur.popcorn.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vladabur.popcorn.data.database.entities.MovieEntity

@Dao
interface MovieDao {
    
    @Query("SELECT * FROM ${MovieEntity.ENTITY_NAME}")
    fun pagingSource(): PagingSource<Int, MovieEntity>
    
    @Upsert
    suspend fun upsertAll(characters: List<MovieEntity>)

    @Query("DELETE FROM ${MovieEntity.ENTITY_NAME}")
    suspend fun clearAll()
}