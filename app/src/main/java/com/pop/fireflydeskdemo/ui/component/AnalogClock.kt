package com.pop.fireflydeskdemo.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ui.theme.FireFlyDeskDemoTheme
import com.pop.fireflydeskdemo.ui.theme.Lime
import com.pop.fireflydeskdemo.ui.theme.Night
import com.pop.fireflydeskdemo.ui.theme.Orange
import com.pop.fireflydeskdemo.ui.theme.Sky
import kotlinx.coroutines.delay
import java.time.LocalTime
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnalogClock(modifier: Modifier = Modifier) {
    val time by produceState(initialValue = LocalTime.now()) {
        while (true) {
            value = LocalTime.now()
            delay(1000L) // 每秒刷新一次
        }
    }

//    val size = DpSize(1000.px.dp, 1000.px.dp)

    Canvas(modifier = modifier
        .background(color = Sky, shape = RoundedCornerShape(50))
    ) {

        val centerX = size.width / 2F
        val centerY = size.height / 2F + 100
        val radius = size.width / 2

        val hour = time.hour % 12
        val minute = time.minute
        val second = time.second

        // 角度计算
        val hourAngle = (hour + minute / 60f) * 30f     // 每小时30°
        val minuteAngle = (minute + second / 60f) * 6f  // 每分钟6°
        val secondAngle = second * 6f                   // 每秒6°


        // 时针
        drawHand(centerX, centerY, radius * 0.4f, hourAngle, Night, strokeWidth = 12f)

        // 分针
        drawHand(centerX, centerY, radius * 0.5f, minuteAngle, Lime, strokeWidth = 8f)

        // 秒针
        drawHand(centerX, centerY, radius * 0.6f, secondAngle, Orange, strokeWidth = 4f)

        // 中心点
        drawCircle(Sky, radius = 20f, center = Offset(centerX, centerY))
    }
}

private fun DrawScope.drawHand(
    centerX: Float,
    centerY: Float,
    length: Float,
    angleDegrees: Float,
    color: Color,
    strokeWidth: Float
) {
    val angleRad = Math.toRadians(angleDegrees - 90.0).toFloat() // 起点在12点方向
    val start = Offset(centerX, centerY)
    val end = Offset(
        x = start.x + length * cos(angleRad),
        y = start.y + length * sin(angleRad)
    )
    drawLine(color, start, end, strokeWidth = strokeWidth, cap = StrokeCap.Round)
}


@Composable
@Preview
fun AnalogClockPreview() {
    FireFlyDeskDemoTheme {
        Box {
            AnalogClock(Modifier.size(800.px.dp))
        }
    }
}