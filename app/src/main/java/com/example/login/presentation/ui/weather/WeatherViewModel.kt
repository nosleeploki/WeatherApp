package com.example.login.presentation.ui.weather

import androidx.lifecycle.ViewModel
import com.example.login.data.repository.WeatherRepository
import androidx.lifecycle.viewModelScope
import com.example.login.data.model.ForecastResponse
import com.example.login.data.model.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class WeatherViewModel (private val repository: WeatherRepository): ViewModel() {

    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?> get() = _weatherData

    private val _forecastData = MutableStateFlow<ForecastResponse?>(null)
    val forecastData: StateFlow<ForecastResponse?> get() = _forecastData

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            repository.fetchWeather(city)
                .catch { e ->
                    _error.value = e.message
                }
                .collect { response ->
                    _weatherData.value = response
                }
        }
    }

    fun fetchWeatherForecast(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            repository.fetchWeatherForecast(latitude, longitude)
                .catch { e ->
                    _error.value = e.message
                }
                .collect { response ->
                    _forecastData.value = response
                }
        }
    }
}