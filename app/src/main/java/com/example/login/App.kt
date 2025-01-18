package com.example.login

import android.app.Application
import com.example.login.data.di.apiServiceModule
import com.example.login.data.di.cacheModule
import com.example.login.data.di.coreModule
import com.example.login.data.di.locationModule
import com.example.login.data.di.repoModule
import com.example.login.data.di.retrofitModule
import com.example.login.presentation.di.adapterModule
import com.example.login.presentation.di.uiModelMapperModule
import com.example.login.presentation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    companion object {
        private var instance: App? = null
        fun getInstance() = instance!!
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        setupKoin()
    }


    private fun setupKoin() {
        startKoin {
            androidContext(this@App)
            modules (
                    coreModule,
                    cacheModule,
                    apiServiceModule,
                    retrofitModule,
                    repoModule,
                    viewModelModule,
                    uiModelMapperModule,
                    locationModule,
                    adapterModule
                )

        }
    }
}