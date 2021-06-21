package com.sharkawy.yelloreceiver.entities.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var username: String?,
    var phone: String?
)