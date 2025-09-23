package com.vladabur.popcorn.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vladabur.popcorn.data.database.entities.RemoteKeyEntity

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: RemoteKeyEntity)

    @Query("SELECT * FROM ${RemoteKeyEntity.ENTITY_NAME} WHERE id = 0")
    suspend fun getRemoteKey(): RemoteKeyEntity?

    @Query("DELETE FROM ${RemoteKeyEntity.ENTITY_NAME}")
    suspend fun clearAll()
}