package com.example.login.presentation.di

import com.example.login.data.model.DailyForecastUiModel
import com.example.login.data.model.HourlyForecastUiModel
import com.example.login.presentation.ui.weather.DailyForecastAdapter
import com.example.login.presentation.ui.weather.HourlyForecastAdapter
import org.koin.dsl.module

val adapterModule = module {
    factory { HourlyForecastAdapter() }
    factory { DailyForecastAdapter () }
}