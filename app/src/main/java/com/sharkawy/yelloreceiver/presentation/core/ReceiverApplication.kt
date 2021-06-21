package com.sharkawy.yelloreceiver.presentation.core

import android.app.Application
import com.sharkawy.yelloreceiver.domain.gateways.local.UserDb
import com.sharkawy.yelloreceiver.domain.repositories.user.UserRepositoryImpl

class ReceiverApplication : Application() {
    private val database by lazy { UserDb.getDatabaseClient(this) }
    val repository by lazy { UserRepositoryImpl(database.userDao()) }

    override fun onCreate() {
        super.onCreate()

    }
}