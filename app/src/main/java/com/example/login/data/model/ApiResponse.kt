package com.example.login.data.model

data class WeatherResponse (
    val coord: Coord,
    val weather: List<Weather>,
    val main: Main,
    val name: String,
    val city: City
)

data class ForecastResponse(
    val city: City,
    val list: List<ForecastItem>,
    val dailyItem: List<DailyForecast>? = null,
    val hourlyItem: List<HourlyForecast>? = null
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
    val dt_txt: String
)

data class Main(
    val temp: Double,
    val humidity: Int
)

data class Weather(
    val description: String,
    val icon: String
)

data class DailyForecast(
    val date: String,
    val temperature: String,
    val description: String,
    val iconUrl: String
)

data class HourlyForecast(
    val time: String,
    val temperature: String,
    val iconUrl: String
)


