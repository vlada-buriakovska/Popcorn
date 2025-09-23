package com.vladabur.popcorn.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vladabur.popcorn.data.database.entities.MovieEntity
import com.vladabur.popcorn.data.database.dao.MovieDao
import com.vladabur.popcorn.data.database.dao.RemoteKeyDao
import com.vladabur.popcorn.data.database.entities.RemoteKeyEntity

@Database(
    entities = [MovieEntity::class, RemoteKeyEntity::class],
    version = 6,
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "popcorn.db"
    }

    abstract fun movieDao(): MovieDao
    
    abstract fun remoteKeyDao(): RemoteKeyDao
}