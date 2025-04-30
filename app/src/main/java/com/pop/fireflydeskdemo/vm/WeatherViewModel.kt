package com.pop.fireflydeskdemo.vm

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pop.fireflydeskdemo.ui.theme.Cloud
import com.pop.fireflydeskdemo.ui.theme.Grape
import com.pop.fireflydeskdemo.ui.theme.Lime
import com.pop.fireflydeskdemo.ui.theme.Orange
import com.pop.fireflydeskdemo.ui.theme.Sea
import com.pop.fireflydeskdemo.ui.theme.Sky
import com.pop.fireflydeskdemo.ui.theme.Stone
import com.pop.libnet.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

class WeatherViewModel : ViewModel() {

    companion object {
        private const val TAG = "WeatherViewModel"

        private const val VSC_WEATHER_URL =
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/shanghai/today?unitGroup=metric&include=current&contentType=json"

        private const val KEY = "&key=U3X4N6CDBRVYAH2UF9LY6TUQ7"

    }

    private val allWeatherDetails: List<WeatherDetail> by lazy {
        WeatherDetail::class.sealedSubclasses
            .mapNotNull { it.objectInstance }
    }

    private val _weatherUiState = MutableStateFlow(CurrentHourWeatherUiStateSample)

    val weatherUiState: StateFlow<CurrentHourWeatherUiState> = _weatherUiState.asStateFlow()


    init {
        viewModelScope.launch {
            while (true) {
                _weatherUiState.value = currentWeatherUiState()
                delay(30.seconds)
            }
        }
    }

    private suspend fun currentWeatherUiState(): CurrentHourWeatherUiState {
        val response = HttpClient.get(VSC_WEATHER_URL + KEY) {
            contentType(ContentType.Application.Json)
        }
        Log.e(TAG, "currentWeatherUiState: ${response.bodyAsText()}")
        val json = Json { ignoreUnknownKeys = true }
        val weather = json.decodeFromString<WeatherUiState>(response.bodyAsText())
        Log.e(TAG, "currentWeatherUiState: $weather")
        return CurrentHourWeatherUiState(
            weatherDetail = allWeatherDetails.first { it.icon == weather.currentConditions.icon },
            temp = weather.currentConditions.temp,
        )
    }


}

@Serializable
data class WeatherUiState(
    val currentConditions: CurrentConditions
)

@Serializable
data class CurrentConditions(
    val icon: String,
    val temp: Double
)

data class CurrentHourWeatherUiState(
    val weatherDetail: WeatherDetail,
    val temp: Double
)

sealed class WeatherDetail(
    val icon: String,
    val desc: String,
    val color: Color
) {
    data object Snow : WeatherDetail("snow", "下雪", Cloud)
    data object Rain : WeatherDetail("rain", "雨天", Sea)
    data object Fog : WeatherDetail("fog", "大雾", Stone)
    data object Wind : WeatherDetail("wind", "大风", Lime)
    data object Cloudy : WeatherDetail("cloudy", "阴天", Stone)
    data object PartlyCloudy : WeatherDetail("partly-cloudy-day", "多云", Sky)
    data object PartlyCloudyNight : WeatherDetail("partly-cloudy-night", "多云", Sky)
    data object ClearDay : WeatherDetail("clear-day", "晴天", Orange)
    data object ClearNight : WeatherDetail("clear-night", "晴天", Orange)
}

val CurrentHourWeatherUiStateSample = CurrentHourWeatherUiState(
    weatherDetail = WeatherDetail.ClearDay,
    temp = 18.0,
)