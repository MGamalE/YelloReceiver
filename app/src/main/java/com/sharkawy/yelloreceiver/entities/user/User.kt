package com.sharkawy.yelloreceiver.entities.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    var username: String?,
    var phone: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 1
}