package com.example.login.data.di

import com.example.login.data.model.HourlyForecastEntity
import com.example.login.presentation.ui.weather.DailyForecastAdapter
import com.example.login.presentation.ui.weather.HourlyForecastAdapter
import com.example.login.presentation.ui.weather.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val weatherModule = module {
    //Inject
    viewModel {WeatherViewModel(get(), get(), get())}

    single<FusedLocationProviderClient>{
        LocationServices.getFusedLocationProviderClient(get())
    }

    single { HourlyForecastAdapter() }
    single { DailyForecastAdapter() }
}