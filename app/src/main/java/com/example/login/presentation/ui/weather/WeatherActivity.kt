package com.example.login.presentation.ui.weather

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.login.R
import com.example.login.databinding.WaWeatherActivityBinding
import com.example.login.presentation.Constants.API_KEY

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: WaWeatherActivityBinding
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.wa_weather_activity)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.rvHourlyForecast.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvDailyForecast.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewModel.currentWeather.observe(this, Observer { currentWeather ->
            binding.tvCity.text = currentWeather.cityName
            binding.tvTemperature.text = "${currentWeather.main.temperature}°C"
            binding.tvHumidity.text = "Humidity: ${currentWeather.main.humidity}%"
            binding.tvWeatherDescription.text = currentWeather.weather[0].description

            val iconUrl = "https://openweathermap.org/img/wn/${currentWeather.weather[0].icon}@2x.png"
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
    }
}