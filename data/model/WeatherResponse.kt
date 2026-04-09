package com.example.weatherapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    @SerialName("name")
    val cityName: String,
    val main: MainDto,
    val weather: List<WeatherDto>,
) {
    @Serializable
    data class MainDto(
        val temp: Double,
    )

    @Serializable
    data class WeatherDto(
        val description: String,
        val icon: String,
    )
}

