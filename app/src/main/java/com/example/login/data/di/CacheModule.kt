package com.example.login.data.di

import com.example.login.data.cache.AuthCache
import org.koin.dsl.module

val cacheModule = module {
    single { AuthCache(get(), get()) }

}