package com.example.login.presentation.di

import com.example.login.presentation.model.ForecastUiModelMapper
import com.example.login.presentation.model.WeatherUiModelMapper
import org.koin.dsl.module

val uiModelMapperModule = module {
    factory { WeatherUiModelMapper() }
    factory { ForecastUiModelMapper() }
}