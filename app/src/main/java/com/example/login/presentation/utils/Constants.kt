package com.example.login.presentation.utils

import com.example.login.BuildConfig


object Constants {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    val API_KEY: String = BuildConfig.API_KEY
    const val DATABASE_NAME = "weather_app.db"
    const val SHARED_PREFS_NAME = "AppPreferences"

    // Key cho SharedPreferences
    const val PREF_USER_ID = "user_id"

    // Thời gian chờ trong Retrofit (Timeout)
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L

    // Mã lỗi phổ biến
    const val ERROR_404 = "City not found"
    const val ERROR_401 = "Invalid API Key"
    const val ERROR_UNKNOWN = "Unknown error occurred"
}