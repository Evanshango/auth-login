package com.evans.auth.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evans.auth.data.network.Resource
import com.evans.auth.data.network.UserApi
import com.evans.auth.data.repositories.UserRepository
import com.evans.auth.data.responses.LoginResponse
import com.evans.auth.data.responses.User
import com.evans.auth.databinding.FragmentHomeBinding
import com.evans.auth.ui.base.BaseFragment
import com.evans.auth.ui.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding, UserRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visible(false)

        viewModel.getUser()

        viewModel.user.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar.visible(false)
                    updateUI(it.value.user)
                }
                is Resource.Loading -> {
                    binding.progressBar.visible(true)
                }
                is Resource.Failure -> {
                    binding.progressBar.visible(false)
                }
            }
        })

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun updateUI(user: User) {
        with(binding) {
            txtId.text = user.id.toString()
            txtName.text = user.name
            txtEmail.text = user.email
        }
    }

    override fun getViewModel() = HomeViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): UserRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = dataSource.buildApi(UserApi::class.java, token)
        return UserRepository(api)
    }
}