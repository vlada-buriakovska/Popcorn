package com.vladabur.popcorn.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vladabur.popcorn.data.database.entities.RemoteKeyEntity.Companion.ENTITY_NAME

@Entity(tableName = ENTITY_NAME)
data class RemoteKeyEntity(
    @PrimaryKey
    val id: Int = 0,
    val nextKey: Int?
) {
    companion object {
        const val ENTITY_NAME = "remote_keys"
    }
}