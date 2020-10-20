package com.evans.auth.ui.base

import androidx.lifecycle.ViewModel
import com.evans.auth.data.network.UserApi
import com.evans.auth.data.repositories.BaseRepository

abstract class BaseViewModel(private val repository: BaseRepository): ViewModel() {

    suspend fun logout(api: UserApi){
        repository.logout(api)
    }
}