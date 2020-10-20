package com.evans.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.evans.auth.data.UserPreferences
import com.evans.auth.ui.auth.AuthActivity
import com.evans.auth.ui.home.HomeActivity
import com.evans.auth.ui.startNewActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userPreferences = UserPreferences(this)

        userPreferences.authToken.asLiveData().observe(this, {
            val activity = if (it == null) AuthActivity::class.java else HomeActivity::class.java
            startNewActivity(activity)
        })
    }
}