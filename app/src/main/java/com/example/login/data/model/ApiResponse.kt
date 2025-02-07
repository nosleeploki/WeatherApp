package com.example.login.data.model

import android.health.connect.datatypes.units.Temperature
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("name")
    val cityName: String,
    @SerializedName("main")
    val main: Main,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("coord")
    val coord: Coord
)

data class Coord(
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lon")
    val longitude: Double
)

data class Main(
    @SerializedName("temp")
    val temperature: Double,
    @SerializedName("humidity")
    val humidity: Int
)

data class Weather(
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String
)

data class WeatherForecastResponse(
    @SerializedName("list")
    val forecastList: List<ForecastItem>
)

data class ForecastItem(
    @SerializedName("dt_txt")
    val dateTime: String,
    @SerializedName("main")
    val main: MainForecast,
    @SerializedName("weather")
    val weather: List<Weather>
)

data class MainForecast(
    @SerializedName("temp")
    val temperature: Double,
    @SerializedName("humidity")
    val humidity: Int
)