package com.sharkawy.yelloreceiver.domain.repositories.user

import com.sharkawy.yelloreceiver.domain.gateways.local.UserDao
import com.sharkawy.yelloreceiver.entities.user.User


class UserRepositoryImpl(private val userDao: UserDao):UserRepository{
    override suspend fun insertUser(user: User) =
        userDao.insertUser(user)


    override suspend fun getUser(): User =
        userDao.getUser()


}