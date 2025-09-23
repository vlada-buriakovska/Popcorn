package com.vladabur.popcorn.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vladabur.popcorn.data.database.entities.MovieEntity.Companion.ENTITY_NAME

@Entity(tableName = ENTITY_NAME)
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,
    val id: Int,
    val title: String?,
    val overview: String?,
    val posterPath: String?,
    val releaseDate: String?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val isFavorite: Boolean
) {
    companion object {
        const val ENTITY_NAME = "movies"
    }
}