package com.evans.auth.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evans.auth.data.network.Resource
import com.evans.auth.data.repositories.UserRepository
import com.evans.auth.data.responses.LoginResponse
import com.evans.auth.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : BaseViewModel(repository) {

    private val _user: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val user: LiveData<Resource<LoginResponse>>
        get() = _user

    fun getUser() = viewModelScope.launch {
        _user.value = Resource.Loading
        _user.value = repository.getUser()
    }
}