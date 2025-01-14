package com.example.login.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse (
    val coord: Coord,
    val weather: List<Weather>,
    val main: Main,
    val name: String
)

data class ForecastResponse(
    val city: City,
    val list: List<ForecastItem>
)

data class City(
    val name: String,
    val country: String,
    val coord: Coord
)

data class Coord(
    val lat: Double,
    val lon: Double
)

data class ForecastItem(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind
)

data class Main(
    val temp: Double,
    val humidity: Int
)

data class Weather(
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Double
)