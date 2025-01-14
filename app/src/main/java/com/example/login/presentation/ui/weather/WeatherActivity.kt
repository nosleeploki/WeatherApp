package com.example.login.presentation.ui.weather

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.login.R
import com.example.login.data.local.DatabaseHelper
import com.example.login.data.model.ForecastResponse
import com.example.login.data.network.RetrofitBuilder
import com.example.login.data.repository.WeatherRepository
import com.example.login.databinding.WaWeatherActivityBinding
import com.example.login.presentation.di.WeatherModelFactory
import com.example.login.presentation.ui.login.LoginActivity
import com.example.login.presentation.utils.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.TextUtils
import java.util.Calendar

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: WaWeatherActivityBinding
    private val forecastViewModel: ForecastWeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.wa_weather_activity)
        setContentView(binding.root)

        forecastViewModel.forecastData.observe(this, Observer { forecastResponse ->
            updateUI(forecastResponse)
        })

        forecastViewModel.errorMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })

        val lat = 37.7749
        val lon = -122.4194
        forecastViewModel.fetchWeatherForecast(lat, lon)
    }

    private fun updateUI(forecastResponse: ForecastResponse) {
        val city = forecastResponse.city.name
        val currentWeather = forecastResponse.list.firstOrNull()
        val temperature = currentWeather?.main?.temp ?: "--"
        val humidity = currentWeather?.main?.humidity?.toFloat() ?: "--"
        val description = currentWeather?.weather?.firstOrNull()?.description ?: getString(R.string.unknown)
        binding.tvTemperature.text = getString(R.string.temperature_format, temperature)
        binding.tvHumidity.text = getString(R.string.humidity_format, humidity)
        val weatherIcon = currentWeather?.weather?.firstOrNull()?.icon ?: "01d"

        binding.tvCity.text = city

        binding.tvWeatherDescription.text = description

        val iconUrl = "https://openweathermap.org/img/wn/$weatherIcon@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .placeholder(R.drawable.loading)
            .error(R.drawable.error_image)
            .into(binding.weatherIcon)

        updateHourlyForecast(forecastResponse)
        updateDailyForecast(forecastResponse)
    }
    private fun updateHourlyForecast(forecastResponse: ForecastResponse) {
        binding.hourlyForecastLayoutLayout.removeAllViews()

        forecastResponse.list.take(8).forEach { forecastItem ->
            val hourlyView = layoutInflater.inflate(R.layout.wa_item_hourly, binding.hourlyForecastLayoutLayout, false)

            val timeTextView = hourlyView.findViewById<TextView>(R.id.tvTime)
            val tempTextView = hourlyView.findViewById<TextView>(R.id.tvTemp)
            val iconImageView = hourlyView.findViewById<ImageView>(R.id.ivWeatherIcon)

            val time = Calendar.getInstance().apply {
                timeInMillis = forecastItem.dt * 1000L
            }.get(Calendar.HOUR_OF_DAY)
            timeTextView.text = getString(R.string.hour_format, time)
            tempTextView.text = getString(R.string.temperature_format, forecastItem.main.temp.toInt())

            val iconUrl = "https://openweathermap.org/img/wn/${forecastItem.weather.firstOrNull()?.icon}@2x.png"
            Glide.with(this)
                .load(iconUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error_image)
                .into(iconImageView)

            binding.hourlyForecastLayoutLayout.addView(hourlyView)
        }
    }


    private fun updateDailyForecast(forecastResponse: ForecastResponse) {
        binding.dailyForecastLayout.removeAllViews()

        val dailyForecasts = forecastResponse.list.groupBy { forecastItem ->
            Calendar.getInstance().apply {
                timeInMillis = forecastItem.dt * 1000L
            }.get(Calendar.DAY_OF_YEAR)
        }

        dailyForecasts.forEach { (_, forecastItems) ->
            val dayView = layoutInflater.inflate(R.layout.wa_item_daily, binding.dailyForecastLayout, false)

            val dateTextView = dayView.findViewById<TextView>(R.id.tvDate)
            val tempTextView = dayView.findViewById<TextView>(R.id.tvDailyTemp)
            val iconImageView = dayView.findViewById<ImageView>(R.id.ivDailyWeatherIcon)

            val date = Calendar.getInstance().apply {
                timeInMillis = forecastItems.first().dt * 1000L
            }
            val dayOfMonth = date.get(Calendar.DAY_OF_MONTH)
            val month = date.get(Calendar.MONTH) + 1
            dateTextView.text = getString(R.string.date_format, dayOfMonth, month)

            val averageTemp = forecastItems.map { it.main.temp }.average()
            tempTextView.text = getString(R.string.temperature_format, averageTemp.toInt())

            val iconUrl = "https://openweathermap.org/img/wn/${forecastItems.first().weather.firstOrNull()?.icon}@2x.png"
            Glide.with(this)
                .load(iconUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error_image)
                .into(iconImageView)

            binding.dailyForecastLayout.addView(dayView)
        }
    }
}