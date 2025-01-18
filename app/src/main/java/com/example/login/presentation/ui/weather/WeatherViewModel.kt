package com.example.login.presentation.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.login.BuildConfig
import com.example.login.data.model.ForecastUiModel
import com.example.login.data.model.WeatherUiModel
import com.example.login.data.repository.WeatherRepository
import com.example.login.presentation.model.ForecastUiModelMapper
import com.example.login.presentation.model.WeatherUiModelMapper
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository,
    private val weatherUiModelMapper: WeatherUiModelMapper,
    private val forecastUiModelMapper: ForecastUiModelMapper

    ) : ViewModel() {
    private val _weatherLiveData = MutableLiveData<WeatherUiModel>()
    val weatherLiveData: LiveData<WeatherUiModel> get() = _weatherLiveData

    private val _forecastLiveData = MutableLiveData<ForecastUiModel>()
    val forecastLiveData: LiveData<ForecastUiModel> get() = _forecastLiveData

    val errorMessage = MutableLiveData<String>()

    fun fetchWeather(city: String, apiKey:String = BuildConfig.API_KEY) {
        viewModelScope.launch {
                repository.fetchWeather(city)
                    .map { weatherUiModelMapper.mapToUiModel(it) }
                    .collect { _weatherLiveData.postValue(it)}
                }
        }

    fun fetchWeatherForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            repository.fetchWeatherForecast(lat, lon)
                .map { forecastUiModelMapper.mapToUiModel(it) }
                .collect {_forecastLiveData.postValue(it)}
        }
    }
}
