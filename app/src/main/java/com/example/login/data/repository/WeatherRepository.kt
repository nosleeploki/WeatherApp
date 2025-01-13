package com.example.login.data.repository

import com.example.login.data.local.DatabaseHelper
import com.example.login.data.model.ForecastResponse
import com.example.login.data.model.WeatherResponse
import com.example.login.data.network.WeatherApi
import com.example.login.presentation.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException

class WeatherRepository(
    private val dbHelper: DatabaseHelper,
    private val weatherApi: WeatherApi,
    private val constants: Constants
    ) {

    fun getFavouriteLocations(userID: Int): List<DatabaseHelper.FavoriteLocation> {
        return dbHelper.getFavoriteLocations(userID)
    }

    //Loai bo Call thay bang Flow
    fun fetchWeather(city: String): Flow<WeatherResponse> = flow{
        try {
            val response = weatherApi.getWeather(city, constants.API_KEY)
            //emit de phat tu flow -> subscriber listening
            emit(response)
        } catch (e: IOException){
            throw IOException("Network Error", e)
        } catch (e: HttpException){
            throw HttpException(e.response())
        }
    }

    fun fetchWeatherForecast(latitude: Double, longitude: Double): Flow<ForecastResponse> = flow {
        try {
            val response = weatherApi.getWeatherForecast(latitude, longitude, Constants.API_KEY)
            emit(response)
        } catch (e: IOException) {
            // Xử lý lỗi mạng
            throw IOException("Network error", e)
        } catch (e: HttpException) {
            // Xử lý lỗi HTTP
            throw HttpException(e.response())
        }
    }
}