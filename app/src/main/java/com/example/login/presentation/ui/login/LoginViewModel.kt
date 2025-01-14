package com.example.login.presentation.ui.login

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.login.data.repository.UserRepository

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val isCheck = MutableLiveData<Boolean>()

    val loginStatus = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String?>()

    val navigateToRegisterEvent = MutableLiveData<Boolean>()

    fun onLoginClicked(){
        val usernameValue = username.value.orEmpty()
        val passwordValue = password.value.orEmpty()
        val isCheckValue = isCheck.value ?: false

        if(TextUtils.isEmpty(usernameValue)){
            errorMessage.value="Username is required"
            return
        }
        if(TextUtils.isEmpty(passwordValue)){
            errorMessage.value="Password is required"
            return
        }
        val user = userRepository.loginUser(usernameValue, passwordValue)
        if (user != null) {
            loginStatus.value = true
            errorMessage.value = "Login successful"
            if (isCheckValue) {

            }
        } else {
            loginStatus.value = false
            errorMessage.value = "Wrong username or password!"
        }
    }

    fun navigateToRegister(){
        navigateToRegisterEvent.value = true
    }

}