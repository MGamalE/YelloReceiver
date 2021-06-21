package com.sharkawy.yelloreceiver.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sharkawy.yelloreceiver.domain.repositories.user.UserRepository
import com.sharkawy.yelloreceiver.entities.Resource
import com.sharkawy.yelloreceiver.entities.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    fun getUser() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getUser()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun insertUser(user: User) = viewModelScope.launch {
        repository.insertUser(user)
    }
}


