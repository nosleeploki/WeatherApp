package com.example.login.data.model

//Domain Model
data class WeatherEntity(
    val cityName: String,
    val country: String,
    val temperature: Double,
    val humidity: Int,
    val description: String,
    val iconUrl: String
)

data class ForecastEntity(
    val city: CityEntity,
    val dailyForecasts: List<DailyForecastEntity>,
    val hourlyForecasts: List<HourlyForecastEntity>
)

data class CityEntity(
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
)

data class DailyForecastEntity(
    val date: String,
    val temperature: String,
    val description: String,
    val iconUrl: String
)

data class HourlyForecastEntity(
    val time: String,
    val temperature: String,
    val iconUrl: String
)

//UI Model
data class WeatherUiModel(
    val cityName: String,
    val temperatureDisplay: String,
    val description: String,
    val iconUrl: String,
    val humidityDisplay: String
)

data class ForecastUiModel(
    val cityName: String,
    val dailyForecasts: List<DailyForecastUiModel>,
    val hourlyForecasts: List<HourlyForecastUiModel>
)

data class DailyForecastUiModel(
    val date: String,
    val temperature: String,
    val description: String,
    val iconUrl: String
)

data class HourlyForecastUiModel(
    val time: String,
    val temperature: String,
    val iconUrl: String
)
