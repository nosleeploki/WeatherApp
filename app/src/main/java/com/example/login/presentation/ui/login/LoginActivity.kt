package com.example.login.presentation.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.login.R
import com.example.login.data.local.DatabaseHelper
import com.example.login.data.repository.UserRepository
import com.example.login.databinding.WaLoginActivityBinding
import com.example.login.presentation.di.ViewModelFactory
import com.example.login.presentation.ui.register.RegisterActivity
import com.example.login.presentation.ui.weather.WeatherActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: WaLoginActivityBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.wa_login_activity)

        val dbHelper = DatabaseHelper(this)
        val userRepository = UserRepository(dbHelper)

        val factory = ViewModelFactory(userRepository)

        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.errorMessage.observe(this){
            errorMessage -> errorMessage?.let{
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            viewModel.errorMessage.value = null
        }
        }

        viewModel.loginStatus.observe(this){
            success ->
                if(success){
                    Toast.makeText(this,"Login success!", Toast.LENGTH_SHORT).show()
                }
        }

        viewModel.navigateToRegisterEvent.observe(this) { navigate ->
            if (navigate == true) {
                startActivity(Intent(this, RegisterActivity::class.java))
                viewModel.navigateToRegisterEvent.value = false // Reset sự kiện
            }
        }
    }

    private fun loginUser(username: String, password: String){
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)


        if (userId != -1){
            val dbHelper = DatabaseHelper(this)
            val favoriteLocations = dbHelper.getFavoriteLocations(userId)
        }

        val intent = Intent(this, WeatherActivity::class.java)
        startActivity(intent)
        finish()
    }
}