package com.example.login.presentation.ui.weather

import androidx.lifecycle.ViewModel
import com.example.login.data.repository.WeatherRepository
import androidx.lifecycle.viewModelScope
import com.example.login.data.model.WeatherData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel (private val repository: WeatherRepository): ViewModel() {


}