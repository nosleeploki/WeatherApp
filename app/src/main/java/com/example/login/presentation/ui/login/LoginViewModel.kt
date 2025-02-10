package com.example.login.presentation.ui.login

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.login.data.repository.UserRepository

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    private val isCheck = MutableLiveData<Boolean>()

    val loginStatus = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String?>()

    val navigateToRegisterEvent = MutableLiveData<Boolean>()
    val navigateToWeatherEvent = MutableLiveData<Boolean>()

    fun onLoginClicked(context: Context) {
        val usernameValue = username.value.orEmpty()
        val passwordValue = password.value.orEmpty()
        val isCheckValue = isCheck.value ?: false

        if (TextUtils.isEmpty(usernameValue)) {
            errorMessage.value = "Username is required"
            return
        }
        if (TextUtils.isEmpty(passwordValue)) {
            errorMessage.value = "Password is required"
            return
        }
        val user = userRepository.loginUser(usernameValue, passwordValue)
        if (user != null) {
            loginStatus.value = true
            errorMessage.value = "Login successful"
            saveUserId(context, user.id)
            navigateToWeatherEvent.value = true
            if (isCheckValue) {
                // Save user info or token for future sessions
            }
        } else {
            loginStatus.value = false
            errorMessage.value = "Wrong username or password!"
        }
    }

    private fun saveUserId(context: Context, userId: Int) {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt("user_id", userId).apply()
    }

    fun navigateToRegister() {
        navigateToRegisterEvent.value = true
    }
}