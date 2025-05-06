package com.pop.fireflydeskdemo.ui.main_component

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ui.theme.AppTheme
import com.pop.fireflydeskdemo.vm.DateViewModel
import kotlin.math.cos
import kotlin.math.sin

private const val TAG = "AnalogClock"

@Composable
fun AnalogClock(
    modifier: Modifier = Modifier,
    dateTimeUiState: DateViewModel.DateTimeUiState,
    containerColor: Color,
    contentColor: Color,
    primaryColor: Color,
    secondaryColor: Color,
    tertiaryColor: Color
) {


    Box(modifier.background(containerColor, MaterialTheme.shapes.extraLarge)) {

        Canvas(
            modifier = Modifier
                .size(1500.px.dp)
                .background(
                    color = contentColor,
                    shape = RoundedCornerShape(50)
                )
                .align(Alignment.BottomCenter)
        ) {

            val centerX = size.width / 2F
            val centerY = size.height / 2F
            val radius = size.width / 2


            with(
                dateTimeUiState
            ) {

                // 角度计算
                val hourAngle = (hour + minute / 60f) * 30f     // 每小时30°
                val minuteAngle = (minute + second / 60f) * 6f  // 每分钟6°
                val secondAngle = second * 6f                   // 每秒6°

                Log.e(TAG, "AnalogClock hour: $hour minute: $minute second: $second")
                Log.e(
                    TAG,
                    "AnalogClock hourAngle: $hourAngle minuteAngle: $minuteAngle secondAngle: $secondAngle"
                )

                // 时针
                drawHand(
                    centerX,
                    centerY,
                    radius * 0.6f,
                    hourAngle,
                    tertiaryColor,
                    strokeWidth = 20f
                )

                // 分针
                drawHand(
                    centerX,
                    centerY,
                    radius * 0.75f,
                    minuteAngle,
                    secondaryColor,
                    strokeWidth = 15f
                )

                // 秒针
                drawHand(
                    centerX,
                    centerY,
                    radius * 0.9f,
                    secondAngle,
                    primaryColor,
                    strokeWidth = 10F
                )
            }

            // 中心点
            drawCircle(
                containerColor,
                radius = 100F,
                center = Offset(centerX, centerY)
            )
        }
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
@Preview(widthDp = 978, heightDp = 978, showBackground = false)
fun AnalogClockPreview() {
    AppTheme {
        Box {
//            AnalogClock(Modifier.fillMaxSize(), DateTimeUiStateSample)
        }
    }
}