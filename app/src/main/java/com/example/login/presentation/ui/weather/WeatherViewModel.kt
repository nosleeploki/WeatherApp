package com.example.login.presentation.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.login.data.model.CurrentWeatherResponse
import com.example.login.data.model.Weather
import com.example.login.data.model.WeatherForecastResponse
import com.example.login.data.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class WeatherViewModel: ViewModel() {
    private val _currentWeather = MutableLiveData<CurrentWeatherResponse>()
    val currentWeather: LiveData<CurrentWeatherResponse> get() = _currentWeather

    private val _weatherForecast = MutableLiveData<WeatherForecastResponse>()
    val weatherForecast: LiveData<WeatherForecastResponse> get() = _weatherForecast

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    //Fetch Weather Data
    fun fetchWeatherData(city: String, apiKey: String){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val currentWeather = RetrofitInstance.instance.getCurrentWeather(city, apiKey)
                _currentWeather.postValue(currentWeather)

                //get lat lon from currentWeather by city name
                val lat = currentWeather.coord.latitude
                val lon = currentWeather.coord.longitude

                val forecastWeather = RetrofitInstance.instance.getWeatherForecast(lat, lon, apiKey)
                _weatherForecast.postValue(forecastWeather)
            } catch (e: Exception){
                _errorMessage.postValue("Loi khi lay data weather: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}