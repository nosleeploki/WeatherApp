package com.example.login.presentation.di

import com.example.login.presentation.ui.login.LoginViewModel
import com.example.login.presentation.ui.register.RegisterViewModel
import com.example.login.presentation.ui.weather.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { WeatherViewModel(get(), get(), get())}
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
}