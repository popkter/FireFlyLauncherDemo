package com.pop.fireflydeskdemo.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.ui.theme.FireFlyDeskDemoTheme
import com.pop.fireflydeskdemo.ui.theme.Grape
import com.pop.fireflydeskdemo.ui.theme.Mulish
import com.pop.fireflydeskdemo.vm.CurrentHourWeatherUiState
import com.pop.fireflydeskdemo.vm.CurrentHourWeatherUiStateSample


@Composable()
fun RealTimeWeather(
    modifier: Modifier = Modifier,
    currentHourWeatherUiState: CurrentHourWeatherUiState = CurrentHourWeatherUiStateSample
) {
    RealTimeWeather(
        modifier = modifier,
        iconColor = currentHourWeatherUiState.weatherDetail.color,
        fillColor = currentHourWeatherUiState.weatherDetail.background,
        iconRes = currentHourWeatherUiState.weatherDetail.iconRes,
        temp = currentHourWeatherUiState.temp,
        desc = currentHourWeatherUiState.weatherDetail.desc
    )
}

@Composable
fun RealTimeWeather(
    modifier: Modifier = Modifier,
    iconColor: Color,
    fillColor: Color,
    iconRes: Int,
    temp: Double,
    desc: String
) {

    val fillColor by animateColorAsState(
        targetValue = fillColor,
        animationSpec = tween(durationMillis = 500) // 动画时长500ms
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Grape, RoundedCornerShape(50))
    ) {

        Box(
            Modifier
                .size(1500.px.dp)
                .background(fillColor, RoundedCornerShape(50))
                .align(Alignment.Center)
        ) {

            AnimatedContent(
                targetState = iconColor to iconRes,
                transitionSpec = {
                    // 定义进入和退出动画
                    (slideInVertically { height -> -height } + fadeIn()).togetherWith(
                        slideOutVertically { height -> height } + fadeOut())
                }, modifier = Modifier
                    .offset(x = 250.px.dp, y = 310.px.dp)
                    .size(500.px.dp)
            ) { (iconColor, res) ->
                Icon(
                    painter = painterResource(res),
                    contentDescription = "",
                    tint = iconColor,
                )
            }

        }


        AnimatedContent(
            targetState = iconColor to temp,
            transitionSpec = {
                // 定义进入和退出动画
                (slideInVertically { height -> -height } + fadeIn()).togetherWith(slideOutVertically { height -> height } + fadeOut())
            },
            modifier = Modifier
                .padding(start = 582.px.dp, bottom = 540.px.dp)
                .align(Alignment.BottomStart)
        ) { (iconColor, temp) ->

            //温度
            Text(
                text = "${temp}℃",
                fontSize = 180.px.sp,
                fontFamily = Mulish,
                fontWeight = FontWeight.Light,
                color = iconColor
            )

        }



        AnimatedContent(
            targetState = iconColor to desc,
            transitionSpec = {
                // 定义进入和退出动画
                (slideInVertically { height -> -height } + fadeIn()).togetherWith(slideOutVertically { height -> height } + fadeOut())
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 780.px.dp, end = 420.px.dp)
        ) { (iconColor, desc) ->

            //描述
            Text(
                text = desc,
                fontSize = 240.px.sp,
                fontFamily = Mulish,
                color = iconColor
            )


        }


        Box(
            modifier = Modifier
                .offset(x = -680.px.dp, y = -680.px.dp)
                .size(100.px.dp)
                .background(Grape, RoundedCornerShape(50))
                .align(Alignment.BottomEnd)
        )
    }
}


@Composable
@Preview(
    widthDp = 850, heightDp = 850
)
fun RealTimeWeatherPreview() {
    FireFlyDeskDemoTheme {
        Box {
            RealTimeWeather()
        }
    }
}