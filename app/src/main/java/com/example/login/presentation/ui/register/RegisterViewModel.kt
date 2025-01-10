package com.example.login.presentation.ui.register

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.login.data.repository.UserRepository

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    val name = MutableLiveData<String>()
    val username = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val isAgree = MutableLiveData<Boolean>()

    val registerStatus = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String?>()

    val navigateToLoginEvent = MutableLiveData<Boolean>()

    fun onRegisterClicked(){
        val nameValue = name.value.orEmpty()
        val usernameValue = username.value.orEmpty()
        val phoneValue = phoneNumber.value.orEmpty()
        val passwordValue = password.value.orEmpty()
        val isAgreeValue = isAgree.value ?: false

        if(TextUtils.isEmpty(nameValue)){
                errorMessage.value="Name is required"
            return
        }
        if(TextUtils.isEmpty(usernameValue)){
            errorMessage.value="Username is required"
            return
        }
        if(TextUtils.isEmpty(phoneValue)){
            errorMessage.value="Phone number is required"
            return
        }
        if(TextUtils.isEmpty(passwordValue)){
            errorMessage.value="Password is required"
            return
        }
        if(usernameValue.length < 6){
            errorMessage.value="Username must be larger than 6 characters"
            return
        }
        if (!usernameValue.matches(Regex("^[a-zA-Z0-9_]{6,30}\$"))) {
            errorMessage.value = "Username must be alphanumeric and between 6-30 characters."
            return
        }
        if (!phoneValue.matches(Regex("^\\d{10}\$"))) {
            errorMessage.value = "Phone number must be exactly 10 digits."
            return
        }
        if(phoneValue.length != 10){
            errorMessage.value="Phone number must be 10 numbers"
            return
        }
        if (!isAgreeValue) {
            errorMessage.value = "Please agree to the terms and conditions."
            return
        }
        if (userRepository.isValid(usernameValue, phoneValue)) {
            errorMessage.value = "Username or phone number already exists."
            return
        }
        val success = userRepository.registerUser(nameValue, usernameValue, phoneValue, passwordValue)
        if (success) {
            registerStatus.value = true
            errorMessage.value = "Registration successful!"
        } else {
            registerStatus.value = false
            errorMessage.value = "Sign up failed. Please try again."
        }
    }

    fun navigateToLogin() {
        navigateToLoginEvent.value = true
    }
}
