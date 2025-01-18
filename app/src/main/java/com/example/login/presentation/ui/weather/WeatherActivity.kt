package com.example.login.presentation.ui.weather

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.login.R
import com.example.login.data.model.ForecastUiModel
import com.example.login.data.model.WeatherUiModel
import com.example.login.databinding.WaWeatherActivityBinding
import com.example.login.presentation.utils.PermissionUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: WaWeatherActivityBinding
    private val weatherViewModel: WeatherViewModel by viewModel()
    private val locationPermissionRequestCode = 1001
    private val hourlyAdapter: HourlyForecastAdapter by inject()
    private val dailyAdapter: DailyForecastAdapter by inject()
    private val fusedLocationClient: FusedLocationProviderClient by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.wa_weather_activity)

        if (PermissionUtils.isPermissionsGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            getUserLocation()
        } else {
            PermissionUtils.requestPermission(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionRequestCode)
        }

        observeViewModel()
        setupRecyclerViews()

        binding.etSearchLocation.setOnEditorActionListener { v, actionId, event ->
            val location = binding.etSearchLocation.text.toString()
            if (location.isNotEmpty()){
                hourlyAdapter.submitList(emptyList())
                dailyAdapter.submitList(emptyList())

                val capitalizedLocation = location.split(" ").joinToString(" ") {
                    it.replaceFirstChar {char -> char.uppercase()}
                }
                weatherViewModel.fetchWeather(location)
                binding.tvCity.text = capitalizedLocation
                true
            } else {
                Toast.makeText(this, "Please write your city", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    private fun observeViewModel(){
        weatherViewModel.weatherLiveData.observe(this, Observer{ weather ->
            weather?.let { updateUI(it) }
            })

        weatherViewModel.forecastLiveData.observe(this, Observer{ forecast ->
            forecast?.let {
                updateHourlyForecast(it)
                updateDailyForecast(it)
            }
        })
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
                weatherViewModel.fetchWeatherForecast(lat, lon)
            } else {
                Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerViews() {
        binding.hourlyRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@WeatherActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = hourlyAdapter
        }
        binding.dailyRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@WeatherActivity, LinearLayoutManager.VERTICAL, false)
            adapter = dailyAdapter
        }
    }

    private fun updateUI(weatherUiModel: WeatherUiModel) {
        val city = weatherUiModel.cityName
        val temperature = "${weatherUiModel.temperatureDisplay}°C"
        val humidity = "\uD83D\uDCA7${weatherUiModel.humidityDisplay}%"
        val description = weatherUiModel.description
        val weatherIcon = weatherUiModel.iconUrl

        binding.tvTemperature.text = temperature
        binding.tvHumidity.text = humidity
        binding.tvCity.text = city
        binding.tvWeatherDescription.text = description

        val iconUrl = "http://openweathermap.org/img/wn/$weatherIcon@2x.png"
        Glide.with(this).load(iconUrl)
            .load(weatherIcon)
            .placeholder(R.drawable.loading)
            .error(R.drawable.alerticon)
            .into(binding.weatherIcon)

        Log.d("WeatherActivity", "Updating UI: City = $city, Temperature = $temperature, Humidity = $humidity, Description = $description")
    }

    private fun updateDailyForecast(forecast: ForecastUiModel) {
        dailyAdapter.submitList(forecast.dailyForecasts){
            binding.dailyRecyclerView.scrollToPosition(0)
        }
        Log.d("WeatherActivity", "Updating Daily Forecast: $forecast")

    }

    private fun updateHourlyForecast(forecast: ForecastUiModel) {
        val hourlyData = forecast.hourlyForecasts.take(8)
        hourlyAdapter.submitList(hourlyData){
            binding.hourlyRecyclerView.scrollToPosition(0)
        }
        Log.d("WeatherActivity", "Updating Hourly Forecast: $forecast")
        hourlyAdapter.notifyDataSetChanged()
    }


}