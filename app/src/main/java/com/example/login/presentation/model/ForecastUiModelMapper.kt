package com.example.login.presentation.model

import com.example.login.data.model.DailyForecastUiModel
import com.example.login.data.model.ForecastEntity
import com.example.login.data.model.ForecastUiModel
import com.example.login.data.model.HourlyForecastUiModel

//EntityMap -> WeatherViewModel
class ForecastUiModelMapper {
    fun mapToUiModel(entity: ForecastEntity): ForecastUiModel {
        return ForecastUiModel(
            cityName = entity.city.name,
            dailyForecasts = entity.dailyForecasts.map{
                DailyForecastUiModel(
                    date =it.date,
                    temperature = it.temperature,
                    description = it.description.capitalize(),
                    iconUrl = it.iconUrl
                )
            },
            hourlyForecasts = entity.hourlyForecasts.map {
                HourlyForecastUiModel(
                    time = it.time,
                    temperature = it.temperature,
                    iconUrl = it.iconUrl
                )
            }
        )
    }
}
