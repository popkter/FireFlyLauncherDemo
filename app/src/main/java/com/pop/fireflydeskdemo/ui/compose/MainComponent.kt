package com.pop.fireflydeskdemo.ui.compose

import android.util.Log
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pop.fireflydeskdemo.R
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.ui.main_component.AnalogClock
import com.pop.fireflydeskdemo.ui.main_component.RealTimeWeather
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors
import com.pop.fireflydeskdemo.ui.theme.LocalWeatherColors
import com.pop.fireflydeskdemo.ui.theme.Mulish
import com.pop.fireflydeskdemo.vm.DateViewModel
import com.pop.fireflydeskdemo.vm.WeatherViewModel
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.CLEAR_DAY
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.CLEAR_NIGHT
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.CLOUDY
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.FOG
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.PARTLY_CLOUDY_DAY
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.PARTLY_CLOUDY_NIGHT
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.RAIN
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.SNOW
import com.pop.fireflydeskdemo.vm.WeatherViewModel.Companion.WIND
import kotlin.math.abs

private const val TAG = "MainComponent"

@Composable
fun MainComponent(
    modifier: Modifier = Modifier, dateViewModel: DateViewModel, weatherViewModel: WeatherViewModel
) {

    val dateTimeUiState by dateViewModel.dateTimeUiState.collectAsStateWithLifecycle()

    val weatherUiState by weatherViewModel.weatherUiState.collectAsStateWithLifecycle()

    val naviController =
        listOf(R.drawable.to_home, R.drawable.to_work, R.drawable.to_favorite, R.drawable.to_search)

    val virtualCount = Int.MAX_VALUE

    val actualCount = 4
    //初始图片下标
    val initialIndex = virtualCount / 2

    val pagerState = rememberPagerState(initialPage = initialIndex) { virtualCount }

    val actualPageIndex by remember {
        derivedStateOf {
            (pagerState.currentPage - initialIndex).floorMod(actualCount)
        }
    }

    val controllerMap = remember {
        mapOf(
            0 to naviController, 1 to dateViewModel.controller, 2 to weatherViewModel.controller
        )
    }

    val controller by remember(actualPageIndex) {
        derivedStateOf {
            controllerMap[actualPageIndex] ?: emptyList()
        }
    }

    val fireFlyColors = LocalFireFlyColors.current
    val weatherColors = LocalWeatherColors.current

    Log.e(TAG, "MainComponent actualPageIndex: $actualPageIndex")

    val transition = updateTransition(targetState = actualPageIndex, label = "PageTransition")

    val primaryColor by transition.animateColor(label = "BackgroundColor") { page ->
        when (page) {
            0 -> fireFlyColors.rose
            1 -> fireFlyColors.lime
            2 -> {
                when (weatherUiState.key) {
                    SNOW -> weatherColors.snowyWhite
                    RAIN -> weatherColors.rainyBlue
                    FOG -> weatherColors.foggyBlueGray
                    WIND -> weatherColors.windyGray
                    CLOUDY -> weatherColors.cloudyGray

                    PARTLY_CLOUDY_DAY, PARTLY_CLOUDY_NIGHT -> weatherColors.partlyCloudyWhite

                    CLEAR_DAY, CLEAR_NIGHT -> weatherColors.clearGold

                    else -> weatherColors.cloudyGray
                }
            }

            else -> fireFlyColors.rose
        }

    }

    val secondaryColor by transition.animateColor(label = "ContentColor") { page ->
        when (page) {
            0 -> fireFlyColors.light
            1 -> fireFlyColors.blueSky
            2 -> {
                when (weatherUiState.key) {
                    SNOW -> fireFlyColors.blueSky
                    RAIN -> fireFlyColors.light
                    FOG -> fireFlyColors.darkLoam
                    WIND -> fireFlyColors.light
                    CLOUDY -> fireFlyColors.light

                    PARTLY_CLOUDY_DAY, PARTLY_CLOUDY_NIGHT -> fireFlyColors.blueSky

                    CLEAR_DAY, CLEAR_NIGHT -> fireFlyColors.light

                    else -> fireFlyColors.light
                }
            }

            else -> fireFlyColors.light
        }
    }

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .clipToBounds()
    ) {

        HorizontalPager(
            state = pagerState,
        ) { index ->

            // 计算当前页面与目标页面的偏移量（-1 ~ 1）
            val pageOffset =
                ((pagerState.currentPage - index) + pagerState.currentPageOffsetFraction).coerceIn(
                    -1f, 1f
                )

            // 缩放因子，越靠近当前页 scale 越大
            val scale = 0.6f + (1 - abs(pageOffset)) * 0.4f

            val actualIndex = (index - initialIndex).floorMod(actualCount)

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

                key(actualIndex) {
                    when (actualIndex) {
                        0 -> {
                            Image(
                                painter = painterResource(id = R.drawable.map_capture),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(50)),
                                contentScale = ContentScale.FillWidth
                            )
                        }

                        1 -> {
                            AnalogClock(
                                Modifier.fillMaxSize(),
                                dateTimeUiState,
                                fireFlyColors.blueSky,
                                fireFlyColors.blueSea,
                                fireFlyColors.orange,
                                fireFlyColors.lime,
                                fireFlyColors.night
                            )
                        }

                        2 -> {
                            RealTimeWeather(
                                Modifier.fillMaxSize(),
                                weatherUiState,
                                fireFlyColors.grape,
                                primaryColor,
                                secondaryColor
                            )
                        }
                    }
                }

            }


        }

        // TODO: 根据当前显示的场景修改QcPanel的颜色
        Row(
            modifier = Modifier
                .padding(top = 180.px.dp, end = 50.px.dp)
                .background(
                    color = primaryColor, shape = MaterialTheme.shapes.large
                )
                .align(Alignment.TopEnd)
                .padding(horizontal = 50.px.dp)
                .animateContentSize(),
            horizontalArrangement = Arrangement.End
        ) {
            if (controller.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.height(200.px.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(80.px.dp),
                ) {
                    items(controller, key = { it }) { icon ->
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = "",
                            modifier = Modifier
                                .size(120.px.dp)
                                .clickable {

                                },
                            tint = if (icon == R.drawable.to_warn) Color.Red else secondaryColor
                        )
                    }
                }
            } else {
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
            }
        }
    }
}


fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other = other) * other
}