package com.pop.fireflydeskdemo.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pop.fireflydeskdemo.R
import com.pop.fireflydeskdemo.ext.ColorMate
import com.pop.libnet.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class WeatherViewModel : ViewModel() {

    companion object {
        private const val TAG = "WeatherViewModel"

        private const val VSC_WEATHER_URL =
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/shanghai/today?unitGroup=metric&include=current&contentType=json"

        private const val KEY = "&key=U3X4N6CDBRVYAH2UF9LY6TUQ7"

        internal const val SNOW = "snow"
        internal const val RAIN = "rain"
        internal const val FOG = "fog"
        internal const val WIND = "wind"
        internal const val CLOUDY = "cloudy"
        internal const val PARTLY_CLOUDY_DAY = "partly-cloudy-day"
        internal const val PARTLY_CLOUDY_NIGHT = "partly-cloudy-night"
        internal const val CLEAR_DAY = "clear-day"
        internal const val CLEAR_NIGHT = "clear  -night"
    }

    private val _weatherUiState = MutableStateFlow(WeatherUiStateSample)

    val weatherUiState: StateFlow<WeatherUiState> = _weatherUiState.asStateFlow()

    val controller = mutableListOf(R.drawable.to_play, R.drawable.to_location, R.drawable.to_warn)


//    init {
//        viewModelScope.launch {
//            while (true) {
//                _weatherUiState.value = currentWeatherUiState()
//                delay(30.seconds)
//            }
//        }
//    }

    private suspend fun currentWeatherUiState(): WeatherUiState {
        val response = HttpClient.get(VSC_WEATHER_URL + KEY) {
            contentType(ContentType.Application.Json)
        }
        Log.e(TAG, "currentWeatherUiState: ${response.bodyAsText()}")
        val json = Json { ignoreUnknownKeys = true }
        val weather = json.decodeFromString<WeatherModel>(response.bodyAsText())
        Log.e(TAG, "currentWeatherUiState: $weather")
        return WeatherUiState(
            colorMate = ColorMate.fromKey(weather.currentConditions.icon),
            weatherInfo = WeatherInfo.fromKey(weather.currentConditions.icon)
                .copy(temp = weather.currentConditions.temp),
        )
    }


    fun updateWeather() {
        viewModelScope.launch {
            val data = WeatherInfo.all.random()
            _weatherUiState.value = WeatherUiState(
                weatherInfo = data.copy(temp = (0..30).map { it.toDouble() }.random()),
                colorMate = ColorMate.fromKey(data.key),
            )
        }
    }

    @Serializable
    data class WeatherModel(
        val currentConditions: CurrentConditions
    )

    @Serializable
    data class CurrentConditions(
        val icon: String, val temp: Double
    )


    data class WeatherUiState(
        val colorMate: ColorMate, val weatherInfo: WeatherInfo
    )

    data class WeatherInfo(
        val key: String,
        val desc: String,
        val iconRes: Int,
        val temp: Double = 0.0
    ) {
        companion object {
            val snow = WeatherInfo(SNOW, "下雪", R.drawable.icon_snowy)

            val rain = WeatherInfo(RAIN, "雨天", R.drawable.incon_rainy)

            val fog = WeatherInfo(FOG, "大雾", R.drawable.icon_foggy)

            val wind = WeatherInfo(WIND, "大风", R.drawable.icon_windy)

            val cloudy = WeatherInfo(CLOUDY, "阴天", R.drawable.icon_cloudy)

            val partlyCloudy =
                WeatherInfo(PARTLY_CLOUDY_DAY, "多云", R.drawable.icon_partly_cloudy)

            val partlyCloudyNight =
                WeatherInfo(PARTLY_CLOUDY_NIGHT, "多云", R.drawable.icon_partly_cloudy_night)

            val clearDay = WeatherInfo(CLEAR_DAY, "晴天", R.drawable.icon_clear_day)

            val clearNight = WeatherInfo(CLEAR_NIGHT, "晴夜", R.drawable.icon_clear_night)


            val all = listOf(
                snow, rain, fog, wind, cloudy, partlyCloudy, partlyCloudyNight, clearDay, clearNight
            )

            fun fromKey(key: String): WeatherInfo = all.firstOrNull { it.key == key } ?: error("No WeatherInfo found for key: $key")
        }
    }
}


val WeatherUiStateSample = WeatherViewModel.WeatherUiState(
    weatherInfo = WeatherViewModel.WeatherInfo.snow.copy(temp = 18.0),
    colorMate = ColorMate.Snow,
)