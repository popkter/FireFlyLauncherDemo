package com.pop.fireflydeskdemo.vm

import android.os.Parcelable
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pop.fireflydeskdemo.R
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors
import com.pop.fireflydeskdemo.vm.DriveModeViewModel.Companion.MODE_COMFORT
import com.pop.fireflydeskdemo.vm.DriveModeViewModel.Companion.MODE_ECO
import com.pop.fireflydeskdemo.vm.DriveModeViewModel.Companion.MODE_SPORT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlin.coroutines.CoroutineContext

class DriveModeViewModel : ViewModel() {

    companion object {
        private const val TAG = "DriveModeViewModel"
        internal const val MODE_SPORT = "运动"
        internal const val MODE_ECO = "节能"
        internal const val MODE_COMFORT = "舒适"
        private val sportMode = DriveModeController(MODE_ECO, R.drawable.icon_mode_eco)
        private val ecoMode = DriveModeController(MODE_SPORT, R.drawable.icon_mode_sport)
        private val comfortMode = DriveModeController(MODE_COMFORT, R.drawable.icon_mode_comfortable)
    }

    private val controller = mutableListOf(sportMode, ecoMode, comfortMode,)

    private val _onModeChanged = MutableStateFlow(sportMode)
    val onModeChanged = _onModeChanged.asStateFlow()

    fun updateMode(controller: DriveModeController) {
        launchOnIO {
            Log.e(TAG, "updateMode: $controller")
            // TODO: 向下更新状态
        }
    }

    fun randomMode() {
        launchOnIO {
            _onModeChanged.value = controller.random()
        }
    }

    private val _modeList = MutableStateFlow(controller)

    val modeList = _modeList.asStateFlow()

}


@Composable
fun DriveModeController.getDriveModeIconColor(): Color {
    val fireFlyColors = LocalFireFlyColors.current
    return when (desc) {
        MODE_SPORT -> fireFlyColors.orange
        MODE_ECO -> fireFlyColors.lime
        MODE_COMFORT -> fireFlyColors.blueSea
        else -> fireFlyColors.light
    }
}


fun ViewModel.launchOnIO(
    context: CoroutineContext = Dispatchers.IO,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewModelScope.launch(
        context, start, block
    )
}

fun ViewModel.launchOnMain(
    context: CoroutineContext = Dispatchers.Main,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewModelScope.launch(
        context, start, block
    )
}

@Parcelize
data class DriveModeController(
    val desc: String,
    val iconRes: Int,
) : Parcelable