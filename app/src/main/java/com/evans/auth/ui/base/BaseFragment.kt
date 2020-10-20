package com.evans.auth.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.evans.auth.data.UserPreferences
import com.evans.auth.data.network.RemoteDataSource
import com.evans.auth.data.network.UserApi
import com.evans.auth.data.repositories.BaseRepository
import com.evans.auth.ui.auth.AuthActivity
import com.evans.auth.ui.startNewActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseFragment<VM : BaseViewModel, B : ViewBinding, R : BaseRepository> : Fragment() {

    protected lateinit var userPreferences: UserPreferences
    protected lateinit var binding: B
    protected lateinit var viewModel: VM
    protected val dataSource = RemoteDataSource()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userPreferences = UserPreferences(requireContext())

        binding = getFragmentBinding(inflater, container)
        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this, factory)[getViewModel()]

        lifecycleScope.launch { userPreferences.authToken.first() }

        return binding.root
    }

    fun logout() = lifecycleScope.launch {
        val authToken = userPreferences.authToken.first()
        val api = dataSource.buildApi(UserApi::class.java, authToken)
        viewModel.logout(api)
        userPreferences.clearStorage()

        requireActivity().startNewActivity(AuthActivity::class.java)
    }

    abstract fun getViewModel(): Class<VM>

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    abstract fun getFragmentRepository(): R
}