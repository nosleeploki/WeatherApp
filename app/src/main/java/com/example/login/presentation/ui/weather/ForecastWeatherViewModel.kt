package com.example.login.presentation.ui.weather

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.login.data.model.ForecastResponse
import com.example.login.data.network.RetrofitBuilder
import com.example.login.presentation.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForecastWeatherViewModel(application: Application) : AndroidViewModel(application) {
    val forecastData = MutableLiveData<ForecastResponse>()
    val errorMessage = MutableLiveData<String>()

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