package com.example.login.presentation.ui.weather

import DailyForecastAdapter
import HourlyForecastAdapter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.login.R
import com.example.login.databinding.WaWeatherActivityBinding
import com.example.login.presentation.Constants.API_KEY
import java.security.SecureRandom
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import android.Manifest
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import com.example.login.data.repository.WeatherRepository
import com.example.login.presentation.di.WeatherViewModelFactory
import com.example.login.presentation.ui.login.LoginActivity

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: WaWeatherActivityBinding
    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableTLS()

        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)

        if (userId == -1) {
            Toast.makeText(this, "User not found, please login again", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding = DataBindingUtil.setContentView(this, R.layout.wa_weather_activity)
        binding.lifecycleOwner = this

        val weatherRepository = WeatherRepository()

        val factory = WeatherViewModelFactory(weatherRepository, application)
        viewModel = ViewModelProvider(this, factory).get(WeatherViewModel::class.java)

        binding.viewModel = viewModel

        binding.rvHourlyForecast.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvDailyForecast.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewModel.fetchCurrentLocationWeather(API_KEY, this)

        viewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.weatherIcon.visibility = View.INVISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.weatherIcon.visibility = View.VISIBLE
            }
        })

        viewModel.currentWeather.observe(this, Observer { currentWeather ->
            currentWeather?.let {
                binding.tvCity.text = it.cityName
                binding.tvTemperature.text = "${it.main.temperature}°C"
                binding.tvHumidity.text = "Humidity: ${it.main.humidity}%"
                binding.tvWeatherDescription.text = it.weather[0].description

                val iconUrl = "http://openweathermap.org/img/wn/${it.weather[0].icon}@2x.png"
                Log.d("IconUrl", iconUrl)
                Glide.with(this)
                    .load(iconUrl)
                    .into(binding.weatherIcon)
                viewModel.updateFavoriteIcon(userId, it.cityName, binding.ivFavorite)
            }
        })

        viewModel.weatherForecast.observe(this, Observer { weatherForecast ->
            weatherForecast?.let {
                val hourlyAdapter = HourlyForecastAdapter(it.forecastList)
                binding.rvHourlyForecast.adapter = hourlyAdapter

                val dailyAdapter = DailyForecastAdapter(it.forecastList)
                binding.rvDailyForecast.adapter = dailyAdapter
            }
        })

        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        binding.etSearchLocation.setOnClickListener {
            val cityName = binding.etSearchLocation.text.toString()
            if (cityName.isNotEmpty()) {
                viewModel.fetchWeatherData(cityName, API_KEY)
            } else {
                binding.etSearchLocation.error = "Rewrite your city name!"
            }
        }

        binding.icRecent.setOnClickListener {
            viewModel.fetchCurrentLocationWeather(API_KEY, this)
        }

        binding.ivFavorite.setOnClickListener {
            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val userId = sharedPreferences.getInt("user_id", -1)
            val currentCity = binding.tvCity.text.toString()

            if (currentCity.isNotEmpty()) {
                viewModel.toggleFavoriteLocation(userId, currentCity, binding.ivFavorite)
                viewModel.updateFavoriteIcon(userId, currentCity, binding.ivFavorite)
            } else {
                Toast.makeText(this, "No location data available", Toast.LENGTH_SHORT).show()
            }
        }

        binding.ivMenu.setOnClickListener { view ->
            viewModel.loadFavoriteLocations(userId)
            Log.d("PopupMenu", "Favorite locations: ${viewModel.favoriteLocations.value}")
            val popupMenu = PopupMenu(this, view)
            popupMenu.menu.clear()
            popupMenu.menu.add(0, 0, 0, "Your favorite location").isEnabled = false

            viewModel.favoriteLocations.observe(this, Observer { locations ->
                Log.d("FavoriteLocations", "Locations: ${locations?.size}")
                locations?.forEachIndexed { index, location ->
                    popupMenu.menu.add(0, location.id, index, location.locationName)
                    Log.d("FavoriteLocations", "Location: ${location.locationName}")
                }
            })

            val SIGNOUT_MENU_ID = 9999
            popupMenu.menu.add(0, SIGNOUT_MENU_ID, viewModel.favoriteLocations.value?.size ?: 0 + 1, "Signout")

            popupMenu.setOnMenuItemClickListener { item ->
                val selectedLocation = viewModel.favoriteLocations.value?.getOrNull(item.itemId)
                if (selectedLocation != null) {
                    viewModel.fetchWeatherData(selectedLocation.locationName, API_KEY)
                    Toast.makeText(this, "Hiển thị thời tiết cho: ${selectedLocation.locationName}", Toast.LENGTH_SHORT).show()
                } else if (item.itemId == SIGNOUT_MENU_ID) {
                    getSharedPreferences("UserPrefs", MODE_PRIVATE).edit().remove("user_id").apply()
                    Toast.makeText(this, "Sign out Success", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                true
            }
            popupMenu.show()
        }
    }

    private fun enableTLS() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            try {
                val sslContext = SSLContext.getInstance("TLSv1.2")
                sslContext.init(null, null, SecureRandom())
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}