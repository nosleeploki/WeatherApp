package com.example.login.data.network

import com.example.login.data.model.ForecastEntity
import com.example.login.data.model.WeatherEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface  WeatherApi {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city:String,
        @Query("appid") apiKey:String,
        @Query("units") units: String = "metric"
    ): Response<WeatherEntity>

    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("lat") latitude:Double,
        @Query("lon") longtitiude:Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Response<ForecastEntity>
}