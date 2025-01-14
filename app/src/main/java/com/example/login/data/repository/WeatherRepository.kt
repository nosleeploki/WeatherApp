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

        suspend fun fetchWeather(city: String): Flow<WeatherResponse> = flow {
            emit(
                handleApiCall {
                    val response = weatherApi.getWeather(city, constants.API_KEY)
                    if (response.isSuccessful) response.body()!!
                    else throw HttpException(response)
                }
            )
        }

        suspend fun fetchWeatherForecast(latitude: Double, longitude: Double): Flow<ForecastResponse> = flow {
            emit(
                handleApiCall {
                    val response = weatherApi.getWeatherForecast(latitude, longitude, constants.API_KEY)
                    if (response.isSuccessful) response.body()!!
                    else throw HttpException(response)
                }
            )
        }

        private inline fun <T> handleApiCall(apiCall: () -> T): T {
            try {
                return apiCall()
            } catch (e: IOException) {
                throw IOException("Network Error: ${e.message}", e)
            } catch (e: HttpException) {
                throw HttpException(e.response())
            }
        }

    }