package com.evans.auth.ui

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import com.evans.auth.data.network.Resource
import com.evans.auth.ui.auth.LoginFragment
import com.evans.auth.ui.base.BaseFragment
import com.google.android.material.snackbar.Snackbar

fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.snackBar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}

fun Fragment.handleApiError(failure: Resource.Failure, retry: (() -> Unit)? = null) {
    when {
        failure.isNetworkError ->
            requireView().snackBar("Please check your connection", retry)

        failure.errorCode == 401 -> {
            if (this is LoginFragment) {
                requireView().snackBar("Invalid login credentials")
            } else {
                (this as BaseFragment<*, *, *>).logout()
            }
        }
        else -> {
            val error = failure.errorBody?.string().toString()
            requireView().snackBar(error)
        }
    }
}