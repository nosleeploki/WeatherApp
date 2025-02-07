package com.example.login.data.network

import com.example.login.data.model.CurrentWeatherResponse
import com.example.login.data.model.WeatherForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface InterfaceApi {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): CurrentWeatherResponse

    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherForecastResponse
}