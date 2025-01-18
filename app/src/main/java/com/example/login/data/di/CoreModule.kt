package com.example.login.data.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {
    factory { androidContext().resources }
}