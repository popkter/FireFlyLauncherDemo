package com.pop.fireflydeskdemo.ui.compose

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import com.pop.fireflydeskdemo.R
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.ui.component.AnalogClock
import com.pop.fireflydeskdemo.ui.component.RealTimeWeather
import com.pop.fireflydeskdemo.ui.theme.FireFlyDeskDemoTheme
import com.pop.fireflydeskdemo.ui.theme.Mulish
import com.pop.fireflydeskdemo.ui.theme.Orange
import com.pop.fireflydeskdemo.ui.theme.PureWhite
import com.pop.fireflydeskdemo.ui.theme.Rose
import com.pop.fireflydeskdemo.ui.theme.componentRadius
import com.pop.fireflydeskdemo.vm.CurrentHourWeatherUiState
import com.pop.fireflydeskdemo.vm.CurrentHourWeatherUiStateSample
import com.pop.fireflydeskdemo.vm.DateTimeUiState
import com.pop.fireflydeskdemo.vm.DateTimeUiStateSample
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.abs

private const val TAG = "MainComponent"
@Composable
fun MainComponent(
    modifier: Modifier = Modifier,
    dateTimeUiState: DateTimeUiState = DateTimeUiStateSample,
    currentHourWeatherUiState: CurrentHourWeatherUiState = CurrentHourWeatherUiStateSample
) {

    val naviController =
        listOf(R.drawable.to_home, R.drawable.to_work, R.drawable.to_favorite, R.drawable.to_search)

    val clockController =
        listOf(R.drawable.to_alarm, R.drawable.to_note, R.drawable.to_relax, R.drawable.to_mute)

    val weatherController = listOf(R.drawable.to_play, R.drawable.to_location, R.drawable.to_warn)

    var controller by remember { mutableStateOf(naviController) }

    val virtualCount = Int.MAX_VALUE

    val actualCount = 4
    //初始图片下标
    val initialIndex = virtualCount / 2

    var actualPageIndex by remember { mutableIntStateOf(0) }

    val pagerState = rememberPagerState(initialPage = initialIndex) { virtualCount }


    Log.e(TAG, "MainComponent actualPageIndex: $actualPageIndex")

    val qcPanelContentColor by animateColorAsState(
        targetValue = if (actualPageIndex == 3) currentHourWeatherUiState.weatherDetail.background else PureWhite
    )
    val qcPanelBackgroundColor by animateColorAsState(
        targetValue = if (actualPageIndex == 3) currentHourWeatherUiState.weatherDetail.color else Rose
    )

    LaunchedEffect(Unit) {

        snapshotFlow { pagerState.currentPage }.collectLatest { index ->
            controller = when ((index - initialIndex).floorMod(actualCount)) {
                0 -> naviController
                1 -> clockController
                2 -> weatherController
                else -> emptyList()
            }
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(componentRadius))
            .clipToBounds()
    ) {

        HorizontalPager(
            state = pagerState,
        ) { index ->

            actualPageIndex = (index - initialIndex).floorMod(actualCount)

            // 计算当前页面与目标页面的偏移量（-1 ~ 1）
            val pageOffset =
                ((pagerState.currentPage - index) + pagerState.currentPageOffsetFraction).coerceIn(
                    -1f,
                    1f
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
                when (actualPageIndex) {
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
                        AnalogClock(Modifier.fillMaxSize(), dateTimeUiState)
                    }

                    2 -> {
                        RealTimeWeather(Modifier.fillMaxSize(), currentHourWeatherUiState)
                    }
                }
            }


        }

        // TODO: 根据当前显示的场景修改QcPanel的颜色
        Row(
            modifier = Modifier
                .padding(top = 180.px.dp, end = 50.px.dp)
                .height(200.px.dp)
                .background(
                    color = qcPanelBackgroundColor,
                    shape = RoundedCornerShape(componentRadius)
                )
                .align(Alignment.TopEnd)
                .padding(horizontal = 50.px.dp)
                .animateContentSize(),
            horizontalArrangement = Arrangement.End
        ) {

            if (controller.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(80.px.dp),
                ) {
                    items(controller) { icon ->
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = "",
                            modifier = Modifier
                                .size(120.px.dp)
                                .clickable {

                                },
                            tint = if (icon == R.drawable.to_warn) Orange else qcPanelContentColor
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "你似乎来到了一片无人的旷野",
                        color = PureWhite,
                        fontSize = 60.px.sp,
                        fontFamily = Mulish,
                        textAlign = TextAlign.Center

                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainComponentPreview() {
    FireFlyDeskDemoTheme {
        Box {
            MainComponent(
                Modifier
                    .offset(x = 220.px.dp, y = -500.px.dp)
                    .then(Modifier.layout { measurable, _ ->
                        // 不传入 parentConstraints，表示忽略父限制
                        val placeable = measurable.measure(Constraints())
                        layout(placeable.width, placeable.height) {
                            placeable.place(0, 0)
                        }
                    })
                    .size(2200.px.dp, 2200.px.dp)
                    .align(Alignment.TopEnd)
            )
        }
    }
}

fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other = other) * other
}