package com.evans.auth.data.repositories

import com.evans.auth.data.network.UserApi

class UserRepository(
    private val api: UserApi,
) : BaseRepository() {

    suspend fun getUser() = safeApiCall {
        api.getUser()
    }
}