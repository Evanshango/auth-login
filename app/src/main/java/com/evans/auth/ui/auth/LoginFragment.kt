package com.evans.auth.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.evans.auth.data.network.AuthApi
import com.evans.auth.data.network.Resource
import com.evans.auth.data.repositories.AuthRepository
import com.evans.auth.databinding.FragmentLoginBinding
import com.evans.auth.ui.base.BaseFragment
import com.evans.auth.ui.handleApiError
import com.evans.auth.ui.home.HomeActivity
import com.evans.auth.ui.startNewActivity
import com.evans.auth.ui.visible
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.progressBar.visible(false)

        viewModel.loginResponse.observe(viewLifecycleOwner, {
            binding.progressBar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveAuthToken(it.value.user.access_token!!)
                        requireActivity().startNewActivity(HomeActivity::class.java)
                    }
                }
                is Resource.Loading -> binding.progressBar.visible(true)
                is Resource.Failure -> handleApiError(it) { login() }
            }
        })

        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        binding.apply {
            val email = edEmail.text.toString().trim()
            val password = edPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) viewModel.login(email, password)
        }
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        AuthRepository(
            dataSource.buildApi(AuthApi::class.java), userPreferences
        )
}