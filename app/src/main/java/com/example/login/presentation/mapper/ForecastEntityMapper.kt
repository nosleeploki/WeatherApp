package com.example.login.presentation.mapper

import com.example.login.data.model.CityEntity
import com.example.login.data.model.DailyForecastEntity
import com.example.login.data.model.ForecastEntity
import com.example.login.data.model.HourlyForecastEntity
import retrofit2.Response

class ForecastEntityMapper {
    fun mapToEntity(response: Response<ForecastEntity>): ForecastEntity {
        val forecastResponse = response.body()
        return if (forecastResponse != null){
            ForecastEntity(
                city = CityEntity(
                name = forecastResponse.city.name,
                country = forecastResponse.city.country,
                latitude = forecastResponse.city.latitude,
                longitude = forecastResponse.city.longitude
            ),
            dailyForecasts = forecastResponse.dailyForecasts.map {
                DailyForecastEntity(
                    date = it.date,
                    temperature = it.temperature,
                    description = it.description,
                    iconUrl = it.iconUrl
                )
            },
            hourlyForecasts = forecastResponse.hourlyForecasts.map {
                HourlyForecastEntity(
                    time = it.time,
                    temperature = it.temperature,
                    iconUrl = it.iconUrl
                )
            }
        )

        } else {
            throw IllegalArgumentException("Response null")
        }
    }
}

