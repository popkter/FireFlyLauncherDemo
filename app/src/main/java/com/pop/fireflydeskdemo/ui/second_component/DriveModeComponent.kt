package com.pop.fireflydeskdemo.ui.second_component

import android.os.Parcelable
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PageSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pop.fireflydeskdemo.R
import com.pop.fireflydeskdemo.ext.distanceBetween
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.launchOnIO
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.ui.compose.floorMod
import com.pop.fireflydeskdemo.ui.ext.InfiniteHorizontalPager
import com.pop.fireflydeskdemo.ui.ext.rememberInfinitePagerState
import com.pop.fireflydeskdemo.ui.second_component.DriveModeViewModel.Companion.MODE_COMFORT
import com.pop.fireflydeskdemo.ui.second_component.DriveModeViewModel.Companion.MODE_ECO
import com.pop.fireflydeskdemo.ui.second_component.DriveModeViewModel.Companion.MODE_SPORT
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors
import com.pop.fireflydeskdemo.ui.theme.LocalTextColors
import com.pop.fireflydeskdemo.ui.theme.Mulish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.parcelize.Parcelize
import kotlin.math.abs


private const val TAG = "DriveModeComponent"
@Composable
fun DriveModeComponent(
    driveModeViewModel: DriveModeViewModel,
) {

    val modeList by driveModeViewModel.modeList.collectAsStateWithLifecycle()
    val onModeChanged by driveModeViewModel.onModeChanged.collectAsStateWithLifecycle()

    val fireFlyColors = LocalFireFlyColors.current
    val textColors = LocalTextColors.current

    val actualCount = modeList.size

    val initialIndex = Int.MAX_VALUE / 2

    val pagerState = rememberInfinitePagerState(initialIndex)


    val actualIndex by remember {
        derivedStateOf {
            (pagerState.currentPage - initialIndex).floorMod(actualCount)
        }
    }

    val currentMode by remember {
        derivedStateOf {
            modeList[actualIndex].also {
                driveModeViewModel.updateMode(it)
            }
        }
    }


    LaunchedEffect(Unit) {
        snapshotFlow { onModeChanged }.collectLatest {
            val offset = modeList.distanceBetween(currentMode,it)
            pagerState.animateScrollToPage(actualIndex + offset)
        }
    }

    // 这个 PageSize 会在后面传给 Pager
    val threePagesPerViewport = object : PageSize {
        override fun Density.calculateMainAxisPageSize(
            availableSpace: Int, pageSpacing: Int,
        ): Int = (availableSpace - 2 * pageSpacing) / 3
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxSize()
            .background(fireFlyColors.blueSky, MaterialTheme.shapes.extraLarge)
    ) {
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 180.px.dp)
                .align(Alignment.TopCenter),
            text = currentMode.desc,
            fontFamily = Mulish,
            fontSize = 120.px.sp,
            color = currentMode.getDriveModeIconColor(),
        )

        InfiniteHorizontalPager(
            actualPageCount = modeList.size,
            state = pagerState,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.Center),
            pageSize = threePagesPerViewport,
            snapPosition = SnapPosition.Center,
        ) { actualIndex, virtualIndex ->

            val mode = modeList[actualIndex]

            // 计算偏移并做 scale/alpha 动画
            val pageOffset =
                ((pagerState.currentPage - virtualIndex) + pagerState.currentPageOffsetFraction).coerceIn(
                    -1f,
                    1f
                )

            val scale = 0.4f + (1 - abs(pageOffset)) * 0.6f
            val alpha = 0.5f + (1 - abs(pageOffset)) * 0.5f

            // TODO: 点击切换
            Icon(
                painter = painterResource(id = mode.iconRes),
                contentDescription = mode.desc,
                tint = mode.getDriveModeIconColor(),
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }
                    .size(300.px.dp)

            )
        }
    }
}


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




@Parcelize
data class DriveModeController(
    val desc: String,
    val iconRes: Int,
) : Parcelable
