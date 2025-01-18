package com.example.login.data.di

import com.example.login.data.local.DatabaseHelper
import com.example.login.data.repository.UserRepository
import com.example.login.data.repository.WeatherRepository
import com.example.login.presentation.mapper.ForecastEntityMapper
import com.example.login.presentation.mapper.WeatherEntityMapper
import org.koin.dsl.module

val repoModule = module {
    //Mappers (Data -> Domain)
    factory { WeatherEntityMapper() }
    factory { ForecastEntityMapper() }

    //Repository (Repository)
    single { WeatherRepository(get(), get(), get(), get())}
    single { UserRepository(get(), get())}

    single { DatabaseHelper(get())}
}