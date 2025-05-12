package com.pop.fireflydeskdemo.launcher.drivemode

import android.os.Parcelable
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PageSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.launchOnIO
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.launcher.drivemode.DriveModeViewModel.Companion.MODE_COMFORT
import com.pop.fireflydeskdemo.launcher.drivemode.DriveModeViewModel.Companion.MODE_ECO
import com.pop.fireflydeskdemo.launcher.drivemode.DriveModeViewModel.Companion.MODE_SPORT
import com.pop.fireflydeskdemo.launcher.floorMod
import com.pop.fireflydeskdemo.ui.ext.InfiniteHorizontalPager
import com.pop.fireflydeskdemo.ui.ext.rememberInfinitePagerState
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors
import com.pop.fireflydeskdemo.ui.theme.LocalTextColors
import com.pop.fireflydeskdemo.ui.theme.Mulish
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject
import kotlin.math.abs


private const val TAG = "DriveModeComponent"

@Composable
fun DriveModeComponent(
    driveModeViewModel: DriveModeViewModel,
) {

    val modeList by driveModeViewModel.modeListUiState.collectAsStateWithLifecycle()

    val modeUiState by driveModeViewModel.modeUiState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()


    val fireFlyColors = LocalFireFlyColors.current
    val textColors = LocalTextColors.current

    val initialIndex = Int.MAX_VALUE / 2

    val pagerState = rememberInfinitePagerState(initialIndex)

    val threePagesPerViewport = object : PageSize {
        override fun Density.calculateMainAxisPageSize(
            availableSpace: Int, pageSpacing: Int,
        ): Int = (availableSpace - 2 * pageSpacing) / 3
    }

    LaunchedEffect(Unit) {
        snapshotFlow { modeUiState }.collectLatest { controller ->

            val currentIndex = (pagerState.currentPage - initialIndex).floorMod(modeList.size)
            val aimIndex = modeList.indexOf(controller)

            val rawDiff = aimIndex - currentIndex

            when {
                rawDiff > modeList.size / 2 -> rawDiff - modeList.size
                rawDiff < -modeList.size / 2 -> rawDiff + modeList.size
                else -> rawDiff
            }.takeIf { it != 0 }?.let {
                pagerState.animateScrollToPage(pagerState.currentPage + it)
            }
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.settledPage }.collectLatest {
            val initialIndex = Int.MAX_VALUE / 2
            val actualIndex = (it - initialIndex).floorMod(modeList.size)
            driveModeViewModel.updateMode(modeList[actualIndex])
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxSize()
            .background(fireFlyColors.blueSky, MaterialTheme.shapes.extraLarge)
    ) {

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

            Column(
                Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }
                    .size(500.px.dp)
                    .align(Alignment.Center)
                    .clickable {
                        scope.launch {
                            pagerState.animateScrollToPage(virtualIndex)
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = mode.desc,
                    fontFamily = Mulish,
                    fontSize = 120.px.sp,
                    color = mode.getDriveModeIconColor(),
                )

                Icon(
                    painter = painterResource(id = mode.iconRes),
                    contentDescription = mode.desc,
                    tint = mode.getDriveModeIconColor(),
                    modifier = Modifier.size(300.px.dp)
                )
            }

        }
    }
}

@HiltViewModel
class DriveModeViewModel @Inject constructor() : ViewModel() {

    companion object {
        private const val TAG = "DriveModeViewModel"
        internal const val MODE_SPORT = "运动"
        internal const val MODE_ECO = "节能"
        internal const val MODE_COMFORT = "舒适"
        private val sportMode = DriveModeController(MODE_ECO, R.drawable.icon_mode_eco)
        private val ecoMode = DriveModeController(MODE_SPORT, R.drawable.icon_mode_sport)
        private val comfortMode =
            DriveModeController(MODE_COMFORT, R.drawable.icon_mode_comfortable)
    }

    private val controller = mutableListOf(sportMode, ecoMode, comfortMode)


    private val _modeListUiState = MutableStateFlow(controller)
    val modeListUiState = _modeListUiState.asStateFlow()

    private val _modeUiState = MutableStateFlow(sportMode)
    val modeUiState = _modeUiState.asStateFlow()

    fun updateMode(controller: DriveModeController) {
        launchOnIO {
            Log.e(TAG, "updateMode: $controller")
            // TODO: 向下更新状态
            _modeUiState.emit(controller)
        }
    }

    fun randomMode() {
        launchOnIO {
            _modeUiState.value = controller.random()
        }
    }

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
