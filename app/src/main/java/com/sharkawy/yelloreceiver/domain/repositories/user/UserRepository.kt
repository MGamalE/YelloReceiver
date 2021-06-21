package com.sharkawy.yelloreceiver.domain.repositories.user

import com.sharkawy.yelloreceiver.entities.user.User


interface UserRepository {
    suspend fun insertUser(user: User)
    suspend fun getUser(): User
}