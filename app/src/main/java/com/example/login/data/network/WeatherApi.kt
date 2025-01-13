package com.example.login.data.network

import com.example.login.data.model.ForecastResponse
import com.example.login.data.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface  WeatherApi {
    @GET("weather")
    fun getWeather(
        @Query("q") city:String,
        @Query("appid") apiKey:String
    ): WeatherResponse

    @GET("forecast")
    fun getWeatherForecast(
        @Query("lat") latitude:Double,
        @Query("lon") longtitiude:Double,
        @Query("appid") apiKey: String
    ): ForecastResponse
}