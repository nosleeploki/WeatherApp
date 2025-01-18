package com.example.login.presentation.model

import com.example.login.data.model.WeatherEntity
import com.example.login.data.model.WeatherUiModel

class WeatherUiModelMapper{
    //EntityMap -> WeatherViewModel
    fun mapToUiModel(entity: WeatherEntity): WeatherUiModel {
        return WeatherUiModel(
            cityName = entity.cityName,
            temperatureDisplay = "${entity.temperature}°C",
            description = entity.description.capitalize(),
            iconUrl = entity.iconUrl,
            humidityDisplay = "Do am: ${entity.humidity}%"
        )
    }
}