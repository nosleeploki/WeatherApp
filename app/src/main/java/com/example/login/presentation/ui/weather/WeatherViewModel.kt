package com.example.login.presentation.ui.weather

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.login.data.model.CurrentWeatherResponse
import com.example.login.data.model.WeatherForecastResponse
import com.example.login.data.repository.WeatherRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application, weatherRepository: WeatherRepository) : ViewModel() {
    private val weatherRepository = WeatherRepository()

    private val _currentWeather = MutableLiveData<CurrentWeatherResponse>()
    val currentWeather: LiveData<CurrentWeatherResponse> get() = _currentWeather

    private val _weatherForecast = MutableLiveData<WeatherForecastResponse>()
    val weatherForecast: LiveData<WeatherForecastResponse> get() = _weatherForecast

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    //Fetch Weather Data
    fun fetchWeatherData(city: String, apiKey: String){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val currentWeather = weatherRepository.getCurrentWeather(city, apiKey)
                _currentWeather.postValue(currentWeather)

                //get lat lon from currentWeather by city name
                val lat = currentWeather.coord.latitude
                val lon = currentWeather.coord.longitude

                val forecastWeather = weatherRepository.getWeatherForecast(lat, lon, apiKey)
                _weatherForecast.postValue(forecastWeather)
            } catch (e: Exception){
                _errorMessage.postValue("Loi khi lay data weather: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun fetchCurrentLocationWeather(apiKey: String, activity: AppCompatActivity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val lat = location.latitude
                val lon = location.longitude
                fetchWeatherDataByLocation(lat, lon, apiKey)
            } else {
                _errorMessage.postValue("Không thể lấy vị trí hiện tại")
            }
        }.addOnFailureListener { e ->
            _errorMessage.postValue("Lỗi khi lấy vị trí: ${e.message}")
        }
    }

    private fun fetchWeatherDataByLocation(lat: Double, lon: Double, apiKey: String){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val weatherForecast = weatherRepository.getWeatherForecast(lat, lon, apiKey)
                _weatherForecast.postValue(weatherForecast)

                val city = weatherForecast.city.cityName

                val currentWeather = weatherRepository.getCurrentWeather(city, apiKey)
                _currentWeather.postValue(currentWeather)
            }catch (e: Exception){
                _errorMessage.postValue("Loi khi lay data weather: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}