package com.pop.fireflydeskdemo.launcher.clock

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
import androidx.lifecycle.viewModelScope
import com.pop.fireflydeskdemo.R
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ui.theme.AppTheme
import com.pop.fireflydeskdemo.vm.base.MainComponentController
import com.pop.fireflydeskdemo.vm.base.MainComponentViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject
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
    tertiaryColor: Color,
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
    strokeWidth: Float,
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


@HiltViewModel
class DateViewModel @Inject constructor() : MainComponentViewModel() {

    companion object {
        internal const val DATE_TIME = "date_time"

        private const val TO_ALARM = "to_alarm"
        private const val TO_NOTE = "to_note"
        private const val TO_RELAX = "to_relax"
        private const val TO_MUTE = "to_mute"
    }

    private val _dateTimeUiState = MutableStateFlow(currentTimeUiState())
    val dateTimeUiState: StateFlow<DateTimeUiState> = _dateTimeUiState.asStateFlow()

    override val controller = listOf(
        MainComponentController(
            TO_ALARM,
            R.drawable.to_alarm
        ),
        MainComponentController(
            TO_NOTE,
            R.drawable.to_note
        ),
        MainComponentController(
            TO_RELAX,
            R.drawable.to_relax
        ),
        MainComponentController(
            TO_MUTE,
            R.drawable.to_mute
        )
    )

    override fun onControllerClick(controller: MainComponentController) {
        when (controller.desc) {
            TO_ALARM -> {}
            TO_NOTE -> {}
            TO_RELAX -> {}
            TO_MUTE -> {}
        }
    }

    init {
        viewModelScope.launch {
            while (true) {
                _dateTimeUiState.value = currentTimeUiState()
                delay(1000L)
            }
        }
    }

    private fun currentTimeUiState(): DateTimeUiState {
        val now = LocalDateTime.now()
        return DateTimeUiState(
            time = now.format(DateTimeFormatter.ofPattern("HH:mm")),
            date = "${now.monthValue}月 ${now.dayOfMonth}日",
            weekday = now.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA),
            hour = now.hour % 12,
            minute = now.minute,
            second = now.second,
            dateTime = now
        )
    }

    data class DateTimeUiState(
        val date: String,      // 如 4月 30日
        val time: String,      // 如 14:25
        val weekday: String,    // 如 星期二
        val hour: Int, val minute: Int, val second: Int, val dateTime: LocalDateTime,
    )
}

val DateTimeUiStateSample = DateViewModel.DateTimeUiState(
    "4月30日", "14:25", "星期二", hour = 2, minute = 25, second = 10, LocalDateTime.now()
)