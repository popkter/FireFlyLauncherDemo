package com.pop.fireflydeskdemo.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.ui.theme.Cloud
import com.pop.fireflydeskdemo.ui.theme.FireFlyDeskDemoTheme
import com.pop.fireflydeskdemo.ui.theme.Grape
import com.pop.fireflydeskdemo.ui.theme.Mulish
import com.pop.fireflydeskdemo.vm.CurrentHourWeatherUiState
import com.pop.fireflydeskdemo.vm.CurrentHourWeatherUiStateSample

@Composable
fun RealTimeWeather(
    modifier: Modifier = Modifier,
    currentHourWeatherUiState: CurrentHourWeatherUiState = CurrentHourWeatherUiStateSample
) {

    val fillColor = currentHourWeatherUiState.weatherDetail.color


    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Grape, shape = RoundedCornerShape(50))
        ) {

            val centerX = size.width / 2F
            val centerY = size.height / 2F

            drawCircle(
                color = Cloud,
                radius = 750F
            )


            //晴天
            drawCircle(fillColor, 250F, Offset(780F, 840F))


//            drawCircle(Grape, radius = 50F, center = Offset(centerX, centerY))
        }

        //温度
        Text(
            text = "${currentHourWeatherUiState.temp}℃",
            fontSize = 180.px.sp,
            fontFamily = Mulish,
            fontWeight = FontWeight.Light,
            color = fillColor,
            modifier = Modifier
                .padding(start = 582.px.dp, bottom = 540.px.dp)
                .align(Alignment.BottomStart)
        )

        //描述
        Text(
            text = currentHourWeatherUiState.weatherDetail.desc,
            fontSize = 240.px.sp,
            fontFamily = Mulish,
            color = fillColor,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 780.px.dp, end = 420.px.dp)
        )

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
@Preview
fun RealTimeWeatherPreview() {
    FireFlyDeskDemoTheme {
        Box {
            RealTimeWeather(Modifier.size(800.px.dp))
        }
    }
}