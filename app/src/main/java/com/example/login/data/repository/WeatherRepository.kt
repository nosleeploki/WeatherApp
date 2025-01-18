    package com.example.login.data.repository

    import com.example.login.BuildConfig
    import com.example.login.data.local.DatabaseHelper
    import com.example.login.data.model.ForecastEntity
    import com.example.login.data.model.WeatherEntity
    import com.example.login.data.network.WeatherApi
    import com.example.login.presentation.mapper.ForecastEntityMapper
    import com.example.login.presentation.mapper.WeatherEntityMapper
    import kotlinx.coroutines.flow.Flow
    import kotlinx.coroutines.flow.catch
    import kotlinx.coroutines.flow.flow
    import kotlinx.coroutines.flow.map
    import okio.IOException
    import retrofit2.HttpException

    class WeatherRepository(
        private val dbHelper: DatabaseHelper,
        private val weatherApi: WeatherApi,
        private val weatherEntityMapper: WeatherEntityMapper,
        private val forecastEntityMapper: ForecastEntityMapper,
        ) {

        fun getFavouriteLocations(userID: Int): List<DatabaseHelper.FavoriteLocation> {
            return dbHelper.getFavoriteLocations(userID)
        }

        suspend fun fetchWeather(city: String): Flow<WeatherEntity> = flow {
            val response = handleApiCall { weatherApi.getWeather(city, BuildConfig.API_KEY) }
            if (response != null) {
                emit(weatherEntityMapper.mapToEntity(response))
            } else {
                throw IOException("Không có dữ liệu thời tiết trả về.")
            }
        }.catch { emit(handleError(it)) }

        suspend fun fetchWeatherForecast(latitude: Double, longitude: Double): Flow<ForecastEntity> = flow {
            val response = handleApiCall { weatherApi.getWeatherForecast(latitude, longitude, BuildConfig.API_KEY) }
            if (response != null) {
                emit(forecastEntityMapper.mapToEntity(response))
            } else {
                throw IOException("Không có dữ liệu dự báo thời tiết trả về.")
            }
        }.catch { emit(handleError(it)) }


        //error api
        private inline fun <T> handleApiCall(apiCall: () -> T): T {
            try {
                return apiCall()
            } catch (e: IOException) {
                throw IOException("Network Error: ${e.message}", e)
            } catch (e: HttpException) {
                throw HttpException(e.response())
            }
        }


        //error flow
        private fun <T> handleError(throwable: Throwable): T {
            throw when (throwable) {
                is IOException -> IOException("Lỗi mạng: ${throwable.message}")
                is HttpException -> HttpException(throwable.response())
                else -> Exception("Lỗi không xác định: ${throwable.message}")
            }
        }

    }