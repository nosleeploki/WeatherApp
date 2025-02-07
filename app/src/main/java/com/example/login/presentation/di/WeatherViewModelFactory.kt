package com.example.login.presentation.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.login.data.repository.WeatherRepository
import com.example.login.presentation.ui.weather.WeatherViewModel

class WeatherViewModelFactory(
    private val weatherRepository: WeatherRepository,
    private val application: Application
): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{

        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(application, weatherRepository) as T
        }
        throw IllegalArgumentException("Lop viewmodel khong xac dinh")
    }
}