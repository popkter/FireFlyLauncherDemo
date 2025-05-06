package com.pop.fireflydeskdemo.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pop.fireflydeskdemo.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class DateViewModel : ViewModel() {

    companion object {
        internal const val DATE_TIME = "date_time"
    }

    private val _dateTimeUiState = MutableStateFlow(currentTimeUiState())
    val dateTimeUiState: StateFlow<DateTimeUiState> = _dateTimeUiState.asStateFlow()

    val controller = mutableListOf(
        R.drawable.to_alarm, R.drawable.to_note, R.drawable.to_relax, R.drawable.to_mute
    )

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
        val hour: Int, val minute: Int, val second: Int, val dateTime: LocalDateTime
    )
}


val DateTimeUiStateSample = DateViewModel.DateTimeUiState(
    "4月30日", "14:25", "星期二", hour = 2, minute = 25, second = 10, LocalDateTime.now()
)

