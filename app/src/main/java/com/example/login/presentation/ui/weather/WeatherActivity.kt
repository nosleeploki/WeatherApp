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
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.login.data.repository.WeatherRepository
import com.example.login.presentation.di.WeatherViewModelFactory

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: WaWeatherActivityBinding
    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableTLS()

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
            binding.tvCity.text = currentWeather.cityName
            binding.tvTemperature.text = "${currentWeather.main.temperature}°C"
            binding.tvHumidity.text = "Humidity: ${currentWeather.main.humidity}%"
            binding.tvWeatherDescription.text = currentWeather.weather[0].description

            val iconUrl = "http://openweathermap.org/img/wn/${currentWeather.weather[0].icon}@2x.png"
            Log.d("IconUrl", iconUrl) // Log the URL to verify it
            Glide.with(this)
                .load(iconUrl)
                .into(binding.weatherIcon)
        })

        viewModel.weatherForecast.observe(this, Observer { weatherForecast ->
            val hourlyAdapter = HourlyForecastAdapter(weatherForecast.forecastList)
            binding.rvHourlyForecast.adapter = hourlyAdapter

            val dailyAdapter = DailyForecastAdapter(weatherForecast.forecastList)
            binding.rvDailyForecast.adapter = dailyAdapter
        })

        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        })

        binding.etSearchLocation.setOnClickListener{
            val  cityName = binding.etSearchLocation.text.toString()
            if (cityName.isNotEmpty()){
                viewModel.fetchWeatherData(cityName, API_KEY)
            } else {
                binding.etSearchLocation.error = "Rewrite your city name!"
            }
        }

        binding.icRecent.setOnClickListener {
            viewModel.fetchCurrentLocationWeather(API_KEY, this)
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