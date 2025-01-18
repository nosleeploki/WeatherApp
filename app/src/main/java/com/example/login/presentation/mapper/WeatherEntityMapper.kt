package com.example.login.presentation.mapper

import com.example.login.data.model.WeatherEntity
import retrofit2.Response

//Current WeatherResponse -> WeatherEntity
class WeatherEntityMapper {
    fun mapToEntity(response: Response<WeatherEntity>): WeatherEntity {
        val weatherResponse = response.body()
        return if (weatherResponse != null) {
            WeatherEntity(
                cityName = weatherResponse.cityName,
                country = weatherResponse.country,
                temperature = weatherResponse.temperature,
                humidity = weatherResponse.humidity,
                description = weatherResponse.description,
                iconUrl = "https://example.com/icons/${weatherResponse.iconUrl}.png"
            )
        } else {
            throw IllegalArgumentException("Response null")
        }
    }



}