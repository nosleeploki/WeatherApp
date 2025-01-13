package com.example.login.presentation.ui.weather

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.login.R
import com.example.login.data.local.DatabaseHelper
import com.example.login.data.model.WeatherData
import com.example.login.data.network.RetrofitInstance
import com.example.login.data.repository.WeatherRepository
import com.example.login.databinding.WaWeatherActivityBinding
import com.example.login.presentation.di.ViewModelFactory
import com.example.login.presentation.di.WeatherModelFactory
import com.google.android.gms.location.LocationServices
import java.util.jar.Manifest

class WeatherActivity : AppCompatActivity() {

}