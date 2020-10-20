package com.evans.auth.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.evans.auth.data.repositories.AuthRepository
import com.evans.auth.data.repositories.BaseRepository
import com.evans.auth.data.repositories.UserRepository
import com.evans.auth.ui.auth.AuthViewModel
import com.evans.auth.ui.home.HomeViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(repository as UserRepository) as T
            else -> throw IllegalArgumentException("ViewModel class not found")
        }
    }
}