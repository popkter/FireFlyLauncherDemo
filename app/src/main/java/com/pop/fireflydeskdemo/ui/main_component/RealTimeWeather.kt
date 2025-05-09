package com.pop.fireflydeskdemo.ui.main_component

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
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.ui.theme.AppTheme
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors
import com.pop.fireflydeskdemo.ui.theme.LocalWeatherColors
import com.pop.fireflydeskdemo.ui.theme.Mulish
import com.pop.fireflydeskdemo.vm.WeatherUiStateSample
import com.pop.fireflydeskdemo.vm.WeatherViewModel
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.CLEAR_DAY
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.CLEAR_NIGHT
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.CLOUDY
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.FOG
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.PARTLY_CLOUDY_DAY
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.PARTLY_CLOUDY_NIGHT
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.RAIN
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.SNOW
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.WIND

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