package com.example.login.presentation.ui.weather

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.login.R
import com.example.login.data.local.DatabaseHelper
import com.example.login.data.model.DailyForecast
import com.example.login.data.model.ForecastItem
import com.example.login.data.model.ForecastResponse
import com.example.login.data.model.HourlyForecast
import com.example.login.data.model.WeatherResponse
import com.example.login.data.network.RetrofitBuilder
import com.example.login.data.repository.WeatherRepository
import com.example.login.databinding.WaWeatherActivityBinding
import com.example.login.presentation.ui.login.LoginActivity
import com.example.login.presentation.utils.Constants
import com.example.login.presentation.utils.PermissionUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.TextUtils
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: WaWeatherActivityBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val forecastViewModel: ForecastWeatherViewModel by viewModels()
    private val locationPermissionRequestCode = 1001
    private lateinit var hourlyAdapter: HourlyForecastAdapter
    private lateinit var dailyAdapter: DailyForecastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.wa_weather_activity)
        setContentView(binding.root)

        binding.etSearchLocation.setOnEditorActionListener { v, actionId, event ->
            val location = binding.etSearchLocation.text.toString()
            if (!android.text.TextUtils.isEmpty(location)) {
                hourlyAdapter.submitList(emptyList())
                dailyAdapter.submitList(emptyList())
                val capitalizedLocation = location.split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }

                forecastViewModel.fetchWeather(location)
                forecastViewModel.forecastData.observe(this, Observer { forecastResponse ->
                    if (forecastResponse != null) {
                        updateUI(forecastResponse)
                        updateHourlyForecast(forecastResponse)
                        updateDailyForecast(forecastResponse)
                        Log.d("WeatherActivity", "Data updated after searching city: $location")
                    } else {
                        Log.d("WeatherActivity", "No data available after searching city: $location")
                    }
                })

                binding.tvCity.text = capitalizedLocation
                true
            } else {
                Toast.makeText(this, "Please write your city", Toast.LENGTH_SHORT).show()
                false
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (PermissionUtils.isPermissionsGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            getUserLocation()
        } else {
            PermissionUtils.requestPermission(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionRequestCode)
        }

        forecastViewModel.forecastData.observe(this, Observer { forecastResponse ->
            if (forecastResponse != null) {
                updateUI(forecastResponse)
                updateHourlyForecast(forecastResponse)
                updateDailyForecast(forecastResponse)
                Log.d("WeatherActivity", "Data updated on app start: $forecastResponse")
            } else {
                Log.d("WeatherActivity", "K co du lieu")

            }
        })
        forecastViewModel.weatherData.observe(this, Observer { weatherResponse ->
            updateCurrentWeatherUI(weatherResponse)
        })

        forecastViewModel.errorMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })

        setupRecyclerViews()
    }

    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            PermissionUtils.requestPermission(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionRequestCode)
            return
        }

        fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<Location> ->
            val location = task.result
            if (location != null) {
                val lat = location.latitude
                val lon = location.longitude
                Log.d("WeatherActivity", "Location retrieved: Latitude = $lat, Longitude = $lon")
                forecastViewModel.fetchWeatherForecast(lat, lon)
            } else {
                Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerViews() {
        hourlyAdapter = HourlyForecastAdapter()
        binding.hourlyRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.hourlyRecyclerView.adapter = hourlyAdapter

        dailyAdapter = DailyForecastAdapter()
        binding.dailyRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.dailyRecyclerView.adapter = dailyAdapter
    }

    private fun updateUI(forecastResponse: ForecastResponse) {
        val city = forecastResponse.city.name
        val currentWeather = forecastResponse.list.firstOrNull()
        val temperature = currentWeather?.main?.temp?.let { "${it}°C" } ?: "--"
        val humidity = currentWeather?.main?.humidity?.let { "\uD83D\uDCA7$it%" } ?: "--"
        val description = currentWeather?.weather?.firstOrNull()?.description ?: getString(R.string.unknown)
        val weatherIcon = currentWeather?.weather?.firstOrNull()?.icon ?: "01d"

        updateBackground(weatherIcon)

        binding.tvTemperature.text = temperature
        binding.tvHumidity.text = humidity
        binding.tvCity.text = city
        binding.tvWeatherDescription.text = description

        val iconUrl = "http://openweathermap.org/img/wn/$weatherIcon@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .placeholder(R.drawable.loading)
            .error(R.drawable.alerticon)
            .into(binding.weatherIcon)

        Log.d("WeatherActivity", "Updating UI: City = $city, Temperature = $temperature, Humidity = $humidity, Description = $description")
    }

    private fun updateBackground(weatherIcon: String) {
        val colorRes = when (weatherIcon) {
            "01d", "01n" -> R.color.sunnyBackground  // Trời nắng (vàng nhạt)
            "02d", "02n" -> R.color.cloudyBackground // Có mây (xám nhạt)
            "09d", "09n", "10d", "10n" -> R.color.rainyBackground // Mưa (xanh đậm)
            "11d", "11n" -> R.color.stormBackground  // Dông bão (tím đậm)
            "13d", "13n" -> R.color.snowyBackground  // Tuyết (trắng/xanh)
            "50d", "50n" -> R.color.foggyBackground  // Sương mù (xám)
            else -> R.color.defaultBackground        // Mặc định
        }

        binding.root.setBackgroundColor(ContextCompat.getColor(this, colorRes))
    }


    private fun updateCurrentWeatherUI(weatherResponse: WeatherResponse) {
        val temperature = "${weatherResponse.main.temp}°C"
        val humidity = "${weatherResponse.main.humidity}%"
        val description = weatherResponse.weather.firstOrNull()?.description ?: "Unknown"

        binding.tvTemperature.text = temperature
        binding.tvHumidity.text = humidity
        binding.tvWeatherDescription.text = description

        val iconUrl = "http://openweathermap.org/img/wn/${weatherResponse.weather.firstOrNull()?.icon}@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .placeholder(R.drawable.loading)
            .error(R.drawable.alerticon)
            .into(binding.weatherIcon)
        Log.d("WeatherActivity", "Updating Current Weather: Temperature = $temperature, Humidity = $humidity, Description = $description")

    }


    private fun updateDailyForecast(forecastResponse: ForecastResponse) {
        val dailyForecasts = mapToDailyForecast(forecastResponse.list)
        dailyAdapter.submitList(dailyForecasts){
            binding.dailyRecyclerView.scrollToPosition(0)
        }
        dailyAdapter.notifyDataSetChanged()
        Log.d("WeatherActivity", "Updating Daily Forecast: $dailyForecasts")

    }

    private fun updateHourlyForecast(forecastResponse: ForecastResponse) {
        val hourlyForecasts = mapToHourlyForecast(forecastResponse.list)
        hourlyAdapter.submitList(hourlyForecasts){
            binding.hourlyRecyclerView.scrollToPosition(0)
        }
        Log.d("WeatherActivity", "Updating Hourly Forecast: $hourlyForecasts")
        hourlyAdapter.notifyDataSetChanged()
    }

    private fun mapToHourlyForecast(list: List<ForecastItem>): List<HourlyForecast> {
        val hourlyForecasts = mutableListOf<HourlyForecast>()
        val timeCurrent = LocalDateTime.now()
        val next24h = timeCurrent.plusHours(24)

        for (forecast in list) {
            val utcDateTime = LocalDateTime.parse(
                forecast.dt_txt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
            val localDateTime = utcDateTime.atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime()

            if (localDateTime.isAfter(timeCurrent) && localDateTime.isBefore(next24h)){
                val formattedTime = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                val temperature = "${forecast.main.temp.toInt()}°C"
                val iconUrl = "http://openweathermap.org/img/wn/${forecast.weather[0].icon}@2x.png"

                hourlyForecasts.add(
                    HourlyForecast(
                        time = formattedTime,
                        temperature = temperature,
                        iconUrl = iconUrl
                    )
                )
            }
        }



        return hourlyForecasts
    }


    private fun mapToDailyForecast(list: List<ForecastItem>): List<DailyForecast> {
        val groupedByDay = list.groupBy { item ->
            item.dt_txt.substring(0, 10)
        }

        return groupedByDay.map { entry ->
            val temperatures = entry.value.map { it.main.temp }
            val averageTemperature = temperatures.average()
            val dayDescription = entry.value[0].weather[0].description
            val icon = entry.value[0].weather[0].icon

            DailyForecast(
                date = entry.key,
                temperature = String.format("%.1f°C", averageTemperature),
                description = dayDescription,
                iconUrl = "http://openweathermap.org/img/wn/$icon@2x.png"
            )
        }
    }
}