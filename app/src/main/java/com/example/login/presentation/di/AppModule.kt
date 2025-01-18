package com.example.login.presentation.di

import com.example.login.data.model.ForecastUiModel
import com.example.login.data.network.RetrofitBuilder
import com.example.login.presentation.mapper.ForecastEntityMapper
import com.example.login.presentation.mapper.WeatherEntityMapper
import com.example.login.presentation.model.WeatherUiModelMapper
import org.koin.dsl.module

val appmodule = module {
    //Retrofit api
    single {RetrofitBuilder.api}

    //Entity Mappers (Data->Domain)
    factory { WeatherEntityMapper() }
    factory { ForecastEntityMapper() }

    //UI Mappers (Domain->UI)
    factory { ForecastUiModel(get(), get(), get()) }
    factory { WeatherUiModelMapper() }
}