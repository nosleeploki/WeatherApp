package com.example.login.presentation.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.login.R
import com.example.login.data.local.DatabaseHelper
import com.example.login.data.repository.UserRepository
import com.example.login.databinding.WaRegisterActivityBinding
import com.example.login.presentation.di.ViewModelFactory
import com.example.login.presentation.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: WaRegisterActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.wa_register_activity)

        val dbHelper = DatabaseHelper(this)
        val userRepository = UserRepository(dbHelper)

        val factory = ViewModelFactory(userRepository)
        viewModel = ViewModelProvider(this, factory).get(RegisterViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.errorMessage.observe(this) {
            errorMessage -> errorMessage?.let{
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            viewModel.errorMessage.value = null
        }
        }

        viewModel.registerStatus.observe(this){
            success ->
                if (success){
                    Toast.makeText(this, "Registration successfull", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    viewModel.navigateToLoginEvent.value = false
                }
        }

        viewModel.navigateToLoginEvent.observe(this) { navigate ->
            if (navigate == true) {
                startActivity(Intent(this, LoginActivity::class.java))
                viewModel.navigateToLoginEvent.value = false
            }
        }
    }
}
