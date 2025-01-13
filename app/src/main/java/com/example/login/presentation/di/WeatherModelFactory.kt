package com.example.login.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.login.data.repository.WeatherRepository
import com.example.login.presentation.ui.weather.WeatherViewModel

class WeatherModelFactory(
    private val repository: WeatherRepository
): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Viewmodel khong xac dinh")
    }

}