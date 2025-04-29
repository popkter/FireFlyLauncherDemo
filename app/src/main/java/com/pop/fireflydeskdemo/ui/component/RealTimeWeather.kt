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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.ui.theme.Cloud
import com.pop.fireflydeskdemo.ui.theme.FireFlyDeskDemoTheme
import com.pop.fireflydeskdemo.ui.theme.Grape
import com.pop.fireflydeskdemo.ui.theme.Lime
import com.pop.fireflydeskdemo.ui.theme.Mulish
import com.pop.fireflydeskdemo.ui.theme.Night
import com.pop.fireflydeskdemo.ui.theme.Orange
import com.pop.fireflydeskdemo.ui.theme.Sky
import kotlinx.coroutines.delay
import java.time.LocalTime
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RealTimeWeather(modifier: Modifier = Modifier) {

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
                radius = size.width / 3
            )

            // 中心点
            drawCircle(Grape, radius = 20f, center = Offset(centerX, centerY))
        }


        Text(
            text = "15℃",
            fontSize = 80.px.sp,
            fontFamily = Mulish,
            color = Orange,
            modifier = Modifier
                .offset(x = -100.px.dp, y = -100.px.dp)
                .align(Alignment.Center)

        )

        Text(
            text = "晴天",
            fontSize = 160.px.sp,
            fontFamily = Mulish,
            color = Orange,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 200.px.dp, start = 200.px.dp)
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