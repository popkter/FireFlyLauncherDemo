package com.pop.fireflydeskdemo.launcher

import android.util.Log
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pop.fireflydeskdemo.R
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.ui.ext.InfiniteHorizontalPager
import com.pop.fireflydeskdemo.ui.ext.rememberInfinitePagerState
import com.pop.fireflydeskdemo.launcher.clock.AnalogClock
import com.pop.fireflydeskdemo.launcher.clock.DateViewModel
import com.pop.fireflydeskdemo.launcher.memo.MemoComponent
import com.pop.fireflydeskdemo.launcher.memo.MemoViewModel
import com.pop.fireflydeskdemo.launcher.map.NaviComponent
import com.pop.fireflydeskdemo.launcher.map.NaviViewModel
import com.pop.fireflydeskdemo.launcher.weather.RealTimeWeather
import com.pop.fireflydeskdemo.launcher.weather.WeatherViewModel
import com.pop.fireflydeskdemo.launcher.weather.getWeatherBackgroundColor
import com.pop.fireflydeskdemo.launcher.weather.getWeatherContentColor
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors
import com.pop.fireflydeskdemo.ui.theme.Mulish
import kotlin.math.abs

private const val TAG = "MainComponent"

@Composable
fun MainComponent(
    modifier: Modifier = Modifier,
    naviViewModel: NaviViewModel,
    dateViewModel: DateViewModel,
    weatherViewModel: WeatherViewModel,
    memoViewModel: MemoViewModel,
) {

    val viewModels = listOf(naviViewModel, dateViewModel, weatherViewModel, memoViewModel)

    val dateTimeUiState by dateViewModel.dateTimeUiState.collectAsStateWithLifecycle()

    val weatherUiState by weatherViewModel.weatherUiState.collectAsStateWithLifecycle()

    val memoUiState by memoViewModel.memoUiState.collectAsStateWithLifecycle()

    val actualCount = 4

    val initialIndex = Int.MAX_VALUE / 2

    val pagerState = rememberInfinitePagerState(initialIndex)

    val actualPageIndex by remember {
        derivedStateOf {
            (pagerState.currentPage - initialIndex).floorMod(actualCount)
        }
    }

    val fireFlyColors = LocalFireFlyColors.current

    Log.e(TAG, "MainComponent actualPageIndex: $actualPageIndex")

    val transition = updateTransition(targetState = actualPageIndex, label = "PageTransition")

    val controllerBackgroundColor by transition.animateColor(label = "BackgroundColor") { page ->
        getControllerBackgroundColor(page, weatherUiState.key)
    }

    val controllerContentColor by transition.animateColor(label = "ContentColor") { page ->
        getControllerContentColor(page, weatherUiState.key)
    }

    val mainComponents = remember {
        listOf<@Composable () -> Unit>(
            {
                NaviComponent(
                    Modifier.fillMaxSize()
                )
            },
            {
                AnalogClock(
                    Modifier.fillMaxSize(),
                    dateTimeUiState,
                    fireFlyColors.blueSky,
                    fireFlyColors.blueSea,
                    fireFlyColors.orange,
                    fireFlyColors.lime,
                    fireFlyColors.night
                )
            },
            {
                RealTimeWeather(
                    Modifier.fillMaxSize(),
                    weatherUiState
                )
            },
            {
                MemoComponent(
                    Modifier.fillMaxSize(),
//                    memoUiState
                )
            },
            {

            }
        )
    }

    val controller by remember {
        derivedStateOf {
            viewModels.getOrNull(actualPageIndex)?.controller ?: emptyList()
        }
    }

    Box(
        modifier = modifier
    ) {
        //Display
        InfiniteHorizontalPager(
            state = pagerState,
            actualPageCount = 4,
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .clipToBounds()
        ) { actualIndex, virtualIndex ->

            // 计算当前页面与目标页面的偏移量（-1 ~ 1）
            val pageOffset =
                ((pagerState.currentPage - virtualIndex) + pagerState.currentPageOffsetFraction).coerceIn(
                    -1f, 1f
                )

            // 缩放因子，越靠近当前页 scale 越大
            val scale = 0.6f + (1 - abs(pageOffset)) * 0.4f


            Box(
                modifier = Modifier
                    .offset(x = 900.px.dp, y = -430.px.dp)
                    .size(2060.px.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        alpha = scale
                    }, contentAlignment = Alignment.TopEnd
            ) {
                mainComponents[actualIndex]()
            }
        }

        //Controller
        Row(
            modifier = Modifier
                .padding(top = 180.px.dp, end = 50.px.dp)
                .background(
                    color = controllerBackgroundColor, shape = MaterialTheme.shapes.large
                )
                .align(Alignment.TopEnd)
                .padding(horizontal = 50.px.dp)
                .animateContentSize(),
            horizontalArrangement = Arrangement.End
        ) {

            if (controller.isEmpty()) {
                Box(
                    modifier = Modifier.height(200.px.dp), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "你似乎来到了一片无人的旷野",
                        color = Color.White,
                        fontSize = 60.px.sp,
                        fontFamily = Mulish,
                        textAlign = TextAlign.Center

                    )
                }
            } else {
                LazyRow(
                    modifier = Modifier.height(200.px.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(80.px.dp),
                ) {
                    items(controller, key = { it }) { (desc, iconRes) ->
                        Icon(
                            painter = painterResource(iconRes),
                            contentDescription = desc,
                            modifier = Modifier
                                .size(120.px.dp)
                                .clickable {

                                },
                            tint = if (iconRes == R.drawable.to_warn) Color.Red else controllerContentColor
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun getControllerBackgroundColor(actualIndex: Int, weatherKey: String = ""): Color {
    val fireFlyColors = LocalFireFlyColors.current
    return when (actualIndex) {
        0 -> fireFlyColors.rose
        1 -> fireFlyColors.lime
        2 -> getWeatherContentColor(weatherKey)
        else -> fireFlyColors.rose
    }
}

@Composable
fun getControllerContentColor(actualIndex: Int, weatherKey: String = ""): Color {
    val fireFlyColors = LocalFireFlyColors.current
    return when (actualIndex) {
        0 -> fireFlyColors.light
        1 -> fireFlyColors.night
        2 -> getWeatherBackgroundColor(weatherKey)
        else -> fireFlyColors.light
    }
}


fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other = other) * other
}