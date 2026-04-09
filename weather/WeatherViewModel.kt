package com.example.weatherapp.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.remote.RetrofitInstance
import com.example.weatherapp.data.remote.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface WeatherUiState {
    data object Loading : WeatherUiState
    data class Success(val weather: WeatherUiModel) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}

data class WeatherUiModel(
    val cityName: String,
    val temperatureC: Double,
    val description: String,
    val iconUrl: String?,
)

class WeatherViewModel(
    private val api: WeatherApi = RetrofitInstance.weatherApi,
    private val apiKey: String = RetrofitInstance.API_KEY,
    private val defaultCity: String = "London",
) : ViewModel() {
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private var lastCity: String = defaultCity

    init {
        fetchWeather(defaultCity)
    }

    fun fetchWeather(city: String) {
        val trimmedCity = city.trim()
        if (trimmedCity.isEmpty()) {
            _uiState.update { WeatherUiState.Error("City cannot be empty.") }
            return
        }

        lastCity = trimmedCity
        _uiState.update { WeatherUiState.Loading }

        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) { api.getCurrentWeather(trimmedCity, apiKey) }
            }.onSuccess { response ->
                _uiState.update { WeatherUiState.Success(response.toUiModel()) }
            }.onFailure { throwable ->
                val message = throwable.message?.takeIf { it.isNotBlank() } ?: "Something went wrong."
                _uiState.update { WeatherUiState.Error(message) }
            }
        }
    }

    fun refresh() {
        fetchWeather(lastCity)
    }

    class Factory(
        private val api: WeatherApi = RetrofitInstance.weatherApi,
        private val apiKey: String = RetrofitInstance.API_KEY,
        private val defaultCity: String = "London",
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                return WeatherViewModel(api = api, apiKey = apiKey, defaultCity = defaultCity) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

private fun WeatherResponse.toUiModel(): WeatherUiModel {
    val firstWeather = weather.firstOrNull()
    val iconUrl = firstWeather?.icon?.takeIf { it.isNotBlank() }?.let { iconCode ->
        "https://openweathermap.org/img/wn/$iconCode@2x.png"
    }

    return WeatherUiModel(
        cityName = cityName,
        temperatureC = main.temp,
        description = firstWeather?.description.orEmpty(),
        iconUrl = iconUrl,
    )
}
