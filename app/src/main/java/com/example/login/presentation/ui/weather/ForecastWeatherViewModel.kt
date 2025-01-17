package com.example.login.presentation.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.login.data.model.ForecastResponse
import com.example.login.data.model.WeatherResponse
import com.example.login.data.network.RetrofitBuilder
import com.example.login.presentation.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForecastWeatherViewModel : ViewModel() {
    val forecastData = MutableLiveData<ForecastResponse>()
    val weatherData = MutableLiveData<WeatherResponse>()
    val errorMessage = MutableLiveData<String>()

    fun fetchWeather(city: String, apiKey: String = Constants.API_KEY) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitBuilder.api.getWeather(city, apiKey)
                }
                if (response.isSuccessful) {
                    weatherData.postValue(response.body())
                } else {
                    errorMessage.postValue("Lỗi: ${response.message()}")
                }
            } catch (e: Exception) {
                errorMessage.postValue("Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun fetchWeatherForecast(lat: Double, lon: Double, apiKey: String = Constants.API_KEY) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitBuilder.api.getWeatherForecast(lat, lon, apiKey)
                }
                if (response.isSuccessful) {
                    forecastData.postValue(response.body())
                } else {
                    errorMessage.postValue("Lỗi: ${response.message()}")
                }
            } catch (e: Exception) {
                errorMessage.postValue("Lỗi kết nối: ${e.message}")
            }
        }
    }
}