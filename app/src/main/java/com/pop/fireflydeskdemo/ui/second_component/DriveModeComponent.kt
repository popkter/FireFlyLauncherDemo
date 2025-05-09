package com.pop.fireflydeskdemo.ui.second_component

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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.ui.compose.floorMod
import com.pop.fireflydeskdemo.ui.ext.InfiniteHorizontalPager
import com.pop.fireflydeskdemo.ui.ext.rememberInfinitePagerState
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors
import com.pop.fireflydeskdemo.ui.theme.LocalTextColors
import com.pop.fireflydeskdemo.ui.theme.Mulish
import com.pop.fireflydeskdemo.vm.DriveModeViewModel
import com.pop.fireflydeskdemo.vm.getDriveModeIconColor
import kotlinx.coroutines.flow.collectLatest
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
            color = textColors.light,
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

fun <T> List<T>.distanceBetween(value1: T, value2: T): Int {
    val index1 = indexOf(value1)
    val index2 = indexOf(value2)

    return if (index1 != -1 && index2 != -1) {
        index2 - index1
    } else {
        0 // 有一个元素没找到
    }
}


