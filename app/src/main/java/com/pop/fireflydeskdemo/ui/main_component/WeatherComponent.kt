package com.pop.fireflydeskdemo.ui.main_component

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewModelScope
import com.pop.fireflydeskdemo.R
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.ui.main_component.WeatherViewModel.Companion.CLEAR_NIGHT
import com.pop.fireflydeskdemo.ui.main_component.WeatherViewModel.Companion.SNOW
import com.pop.fireflydeskdemo.ui.main_component.WeatherViewModel.Companion.RAIN
import com.pop.fireflydeskdemo.ui.main_component.WeatherViewModel.Companion.CLOUDY
import com.pop.fireflydeskdemo.ui.main_component.WeatherViewModel.Companion.CLEAR_DAY
import com.pop.fireflydeskdemo.ui.main_component.WeatherViewModel.Companion.PARTLY_CLOUDY_NIGHT
import com.pop.fireflydeskdemo.ui.main_component.WeatherViewModel.Companion.PARTLY_CLOUDY_DAY
import com.pop.fireflydeskdemo.ui.main_component.WeatherViewModel.Companion.FOG
import com.pop.fireflydeskdemo.ui.main_component.WeatherViewModel.Companion.WIND
import com.pop.fireflydeskdemo.ui.theme.AppTheme
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors
import com.pop.fireflydeskdemo.ui.theme.LocalWeatherColors
import com.pop.fireflydeskdemo.ui.theme.Mulish
import com.pop.fireflydeskdemo.vm.base.MainComponentController
import com.pop.fireflydeskdemo.vm.base.MainComponentViewModel
import com.pop.libnet.HttpClient
import dagger.hilt.android.lifecycle.HiltViewModel
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
import javax.inject.Inject

@Composable
fun RealTimeWeather(
    modifier: Modifier = Modifier,
    weatherUiState: WeatherViewModel.WeatherUiState,
) {

    val fireFlyColors = LocalFireFlyColors.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                fireFlyColors.grape,
                MaterialTheme.shapes.extraLarge
            )
    ) {
        AnimatedContent(
            targetState = weatherUiState.iconRes,
            modifier = Modifier
                .size(1500.px.dp)
                .background(
                    getWeatherBackgroundColor(weatherUiState.key),
                    MaterialTheme.shapes.extraLarge
                )
                .align(Alignment.Center),
            transitionSpec = {
                // 定义进入和退出动画
                (slideInVertically { height -> -height } + fadeIn()).togetherWith(
                    slideOutVertically { height -> -height } + fadeOut())
            }
        ) { res ->

            Box(
                Modifier.fillMaxSize()

            ) {
                Icon(
                    painter = painterResource(res),
                    contentDescription = "",
                    tint = getWeatherContentColor(weatherUiState.key),
                    modifier = Modifier
                        .offset(x = 250.px.dp, y = 310.px.dp)
                        .size(500.px.dp),
                )
            }
        }

        AnimatedContent(
            targetState = weatherUiState.temp,
            transitionSpec = {
                // 定义进入和退出动画
                (slideInVertically { height -> -height } + fadeIn()).togetherWith(slideOutVertically { height -> height } + fadeOut())
            },
            modifier = Modifier
                .padding(start = 582.px.dp, bottom = 540.px.dp)
                .align(Alignment.BottomStart)
        ) { temp ->

            //温度
            Text(
                text = "${temp}℃",
                fontSize = 180.px.sp,
                fontFamily = Mulish,
                fontWeight = FontWeight.Light,
                color = getWeatherContentColor(weatherUiState.key),
            )

        }



        AnimatedContent(
            targetState = weatherUiState.desc,
            transitionSpec = {
                // 定义进入和退出动画
                (slideInVertically { height -> -height } + fadeIn()).togetherWith(slideOutVertically { height -> height } + fadeOut())
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 780.px.dp, end = 420.px.dp)
        ) { desc ->

            //描述
            Text(
                text = desc,
                fontSize = 240.px.sp,
                fontFamily = Mulish,
                color = getWeatherContentColor(weatherUiState.key),
            )
        }
    }
}


@Composable
fun getWeatherBackgroundColor(key: String): Color {
    val fireFlyColors = LocalFireFlyColors.current
    return when (key) {
        SNOW -> fireFlyColors.blueSky
        RAIN -> fireFlyColors.light
        FOG -> fireFlyColors.darkLoam
        WIND -> fireFlyColors.light
        CLOUDY -> fireFlyColors.light

        PARTLY_CLOUDY_DAY, PARTLY_CLOUDY_NIGHT -> fireFlyColors.blueSky

        CLEAR_DAY, CLEAR_NIGHT -> fireFlyColors.light

        else -> fireFlyColors.light
    }
}

@Composable
fun getWeatherContentColor(key: String): Color {
    val weatherColors = LocalWeatherColors.current
    return when (key) {
        SNOW -> weatherColors.snowyWhite
        RAIN -> weatherColors.rainyBlue
        FOG -> weatherColors.foggyBlueGray
        WIND -> weatherColors.windyGray
        CLOUDY -> weatherColors.cloudyGray

        PARTLY_CLOUDY_DAY, PARTLY_CLOUDY_NIGHT -> weatherColors.partlyCloudyWhite

        CLEAR_DAY, CLEAR_NIGHT -> weatherColors.clearGold

        else -> weatherColors.cloudyGray
    }
}


@Composable
@Preview(
    widthDp = 978, heightDp = 978
)
fun RealTimeWeatherPreview() {
    AppTheme {
        Box {
            RealTimeWeather(weatherUiState = WeatherUiStateSample)
        }
    }
}


@HiltViewModel
class WeatherViewModel @Inject constructor() : MainComponentViewModel() {

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
        internal const val CLEAR_NIGHT = "clear-night"

        private const val TO_PLAY = "to_play"
        private const val TO_LOCATION = "to_location"
        private const val TO_WRAN = "to_warn"

    }

    private val _weatherUiState = MutableStateFlow(WeatherUiStateSample)

    val weatherUiState: StateFlow<WeatherUiState> = _weatherUiState.asStateFlow()

    override val controller = listOf(
        MainComponentController(
            TO_PLAY,
            R.drawable.to_play
        ),
        MainComponentController(
            TO_LOCATION,
            R.drawable.to_location
        ),
        MainComponentController(
            TO_WRAN,
            R.drawable.to_warn
        )
    )

    override fun onControllerClick(controller: MainComponentController) {
        when (controller.desc) {
            TO_PLAY -> {}
            TO_LOCATION -> {}
            TO_WRAN -> {}
        }
    }


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
        return WeatherUiState.fromKey(weather.currentConditions.icon)
            .copy(temp = weather.currentConditions.temp)
    }


    fun updateWeather() {
        viewModelScope.launch {
            val data = WeatherUiState.all.random()
            _weatherUiState.value = data.copy(temp = (0..30).map { it.toDouble() }.random())
        }
    }

    @Serializable
    data class WeatherModel(
        val currentConditions: CurrentConditions,
    )

    @Serializable
    data class CurrentConditions(
        val icon: String, val temp: Double,
    )

    data class WeatherUiState(
        val key: String,
        val desc: String,
        val iconRes: Int,
        val temp: Double = 0.0,
    ) {
        companion object {
            val snow = WeatherUiState(SNOW, "雪天", R.drawable.icon_snowy)

            val rain = WeatherUiState(RAIN, "雨天", R.drawable.incon_rainy)

            val fog = WeatherUiState(FOG, "大雾", R.drawable.icon_foggy)

            val wind = WeatherUiState(WIND, "大风", R.drawable.icon_windy)

            val cloudy = WeatherUiState(CLOUDY, "阴天", R.drawable.icon_cloudy)

            val partlyCloudy =
                WeatherUiState(PARTLY_CLOUDY_DAY, "多云", R.drawable.icon_partly_cloudy)

            val partlyCloudyNight =
                WeatherUiState(PARTLY_CLOUDY_NIGHT, "多云", R.drawable.icon_partly_cloudy_night)

            val clearDay = WeatherUiState(CLEAR_DAY, "晴天", R.drawable.icon_clear_day)

            val clearNight = WeatherUiState(CLEAR_NIGHT, "晴夜", R.drawable.icon_clear_night)


            val all = listOf(
                snow, rain, fog, wind, cloudy, partlyCloudy, partlyCloudyNight, clearDay, clearNight
            )

            fun fromKey(key: String): WeatherUiState =
                all.firstOrNull { it.key == key } ?: error("No WeatherInfo found for key: $key")
        }
    }
}


val WeatherUiStateSample = WeatherViewModel.WeatherUiState.snow.copy(temp = 18.0)