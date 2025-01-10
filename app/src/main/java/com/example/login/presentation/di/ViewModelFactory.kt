package com.example.login.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.login.data.repository.UserRepository
import com.example.login.presentation.ui.login.LoginViewModel
import com.example.login.presentation.ui.register.RegisterViewModel

class ViewModelFactory(private val userRepository: UserRepository): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(userRepository) as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T // Đảm bảo rằng LoginViewModel cũng được khởi tạo
        }
        throw IllegalArgumentException("Lop viewmodel khong xac dinh")
    }
}