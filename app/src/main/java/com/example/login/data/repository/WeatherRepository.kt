package com.example.login.data.repository

import com.example.login.data.model.CurrentWeatherResponse
import com.example.login.data.model.WeatherForecastResponse
import com.example.login.data.network.RetrofitInstance

class WeatherRepository {
    suspend fun getCurrentWeather(city: String, apiKey: String): CurrentWeatherResponse {
        return RetrofitInstance.instance.getCurrentWeather(city, apiKey)
    }

    suspend fun getWeatherForecast(lat: Double, lon: Double, apiKey: String): WeatherForecastResponse {
        return RetrofitInstance.instance.getWeatherForecast(lat, lon, apiKey)
    }

}