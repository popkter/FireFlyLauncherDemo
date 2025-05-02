package com.pop.fireflydeskdemo.vm

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.core.util.toClosedRange
import androidx.core.util.toRange
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pop.fireflydeskdemo.ui.theme.BlueSky
import com.pop.fireflydeskdemo.ui.theme.PureWhite
import com.pop.fireflydeskdemo.ui.theme.DarkLoam
import com.pop.fireflydeskdemo.ui.theme.GraySky
import com.pop.fireflydeskdemo.ui.theme.WeatherColors
import com.pop.fireflydeskdemo.ui.theme.WindyGray
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
import com.pop.fireflydeskdemo.R
import com.pop.fireflydeskdemo.ui.theme.Night
import kotlin.random.Random

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


//    init {
//        viewModelScope.launch {
//            while (true) {
//                _weatherUiState.value = currentWeatherUiState()
//                delay(30.seconds)
//            }
//        }
//    }

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


    fun updateWeather(){
        viewModelScope.launch {
            _weatherUiState.value = CurrentHourWeatherUiState(
                weatherDetail = allWeatherDetails.random(),
                temp = (0..30).map { it.toDouble() }.random(),
            )
        }
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
    val color: Color,
    val background: Color,
    val iconRes: Int,
) {
    data object Snow :
        WeatherDetail("snow", "下雪", WeatherColors.SnowyWhite, BlueSky, R.drawable.icon_snowy)

    data object Rain :
        WeatherDetail("rain", "雨天", WeatherColors.RainyBlue, PureWhite, R.drawable.incon_rainy)

    data object Fog :
        WeatherDetail("fog", "大雾", WeatherColors.FoggyBlueGray, DarkLoam, R.drawable.icon_foggy)

    data object Wind :
        WeatherDetail("wind", "大风", WeatherColors.WindyGray, BlueSky, R.drawable.icon_windy)

    data object Cloudy :
        WeatherDetail("cloudy", "阴天", WeatherColors.CloudyGray, PureWhite, R.drawable.icon_cloudy)

    data object PartlyCloudy : WeatherDetail(
        "partly-cloudy-day",
        "多云",
        WeatherColors.PartlyCloudyWhite,
        BlueSky,
        R.drawable.icon_partly_cloudy
    )

    data object PartlyCloudyNight : WeatherDetail(
        "partly-cloudy-night",
        "多云",
        WeatherColors.PartlyCloudyWhite,
        BlueSky,
        R.drawable.icon_partly_cloudy_night
    )

    data object ClearDay : WeatherDetail(
        "clear-day",
        "晴天",
        WeatherColors.SunnyGold,
        PureWhite,
        R.drawable.icon_clear_day
    )

    data object ClearNight : WeatherDetail(
        "clear-night",
        "晴夜",
        WeatherColors.SunnyGold,
        PureWhite,
        R.drawable.icon_clear_night
    )
}

val CurrentHourWeatherUiStateSample = CurrentHourWeatherUiState(
    weatherDetail = WeatherDetail.PartlyCloudy,
    temp = 18.0,
)