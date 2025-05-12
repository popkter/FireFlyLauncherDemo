package com.pop.fireflydeskdemo.launcher

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.launcher.clock.DateViewModel
import com.pop.fireflydeskdemo.launcher.memo.MemoViewModel
import com.pop.fireflydeskdemo.launcher.map.NaviViewModel
import com.pop.fireflydeskdemo.launcher.weather.WeatherViewModel
import com.pop.fireflydeskdemo.launcher.drivemode.DriveModeViewModel
import com.pop.fireflydeskdemo.ui.theme.AppTheme
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors
import com.pop.fireflydeskdemo.ui.theme.LocalTextColors
import com.pop.fireflydeskdemo.ui.theme.Mulish
import com.pop.fireflydeskdemo.ui.theme.TiltWrap
import com.pop.fireflydeskdemo.vm.MusicViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PopLauncher(
    naviToMap: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    dateViewModel: DateViewModel = hiltViewModel<DateViewModel>(),
    weatherViewModel: WeatherViewModel = hiltViewModel<WeatherViewModel>(),
    memoViewModel: MemoViewModel = hiltViewModel<MemoViewModel>(),
    naviViewModel: NaviViewModel = hiltViewModel<NaviViewModel>(),
    musicViewModel: MusicViewModel = hiltViewModel<MusicViewModel>(),
    driveModeViewModel: DriveModeViewModel = hiltViewModel<DriveModeViewModel>(),
) {

    val colorScheme = LocalFireFlyColors.current

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.large)
            .background(
                color = colorScheme.lime
            )
    ) {

        // maxWidth 和 maxHeight 是父容器的约束
        Box(
            modifier = Modifier
                .then(Modifier.layout { measurable, _ ->
                    // 不传入 parentConstraints，表示忽略父限制
                    val placeable = measurable.measure(Constraints())
                    layout(placeable.width, placeable.height) {
                        placeable.place(0, 0)
                    }
                })
                .size(2880.px.dp, 2060.px.dp)
        ) {

            MainComponent(
                Modifier.fillMaxSize(),
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                naviViewModel = naviViewModel,
                dateViewModel = dateViewModel,
                weatherViewModel = weatherViewModel,
                memoViewModel = memoViewModel,
                naviToMap = { naviToMap() }
            )
        }

        //TopBar
        TopBar(
            modifier = Modifier
                .padding(top = 50.px.dp, start = 50.px.dp)
                .widthIn(710.px.dp, 1910.px.dp)
                .align(Alignment.TopStart),
            dateViewModel
        )

        SecondComponent(
            Modifier
                .padding(start = 50.px.dp, bottom = 300.px.dp)
                .size(900.px.dp)
                .align(Alignment.BottomStart),
            musicViewModel,
            driveModeViewModel
        )
    }

}


@Composable
fun TopBar(modifier: Modifier, dateViewModel: DateViewModel) {

    val colorScheme = LocalFireFlyColors.current
    val textColors = LocalTextColors.current

    val dateTimeUiState by dateViewModel.dateTimeUiState.collectAsStateWithLifecycle()

    var showNotice by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        AnimatedVisibility(
            showNotice
        ) {
            //notice
            Row(
                modifier = Modifier
                    .height(200.px.dp)
                    .background(
                        color = colorScheme.blueSea,
                        shape = MaterialTheme.shapes.large
                    )
                    .padding(horizontal = 50.px.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    tint = textColors.light,
                    contentDescription = "",
                    modifier = Modifier.size(90.px.dp),
                )

                Text(
                    text = "测试文本，用于测试通知是否生效",
                    maxLines = 1,
                    fontFamily = Mulish,
                    fontSize = 60.px.sp,
                    color = textColors.light,
                    modifier = Modifier.wrapContentWidth(),
                )
            }
        }


        Text(
            modifier = Modifier
                .wrapContentSize()
                .clickable {
                    showNotice = !showNotice
                },
            text = dateTimeUiState.time,
            fontFamily = TiltWrap,
            fontSize = 240.px.sp,
            lineHeight = 240.px.sp,
            textAlign = TextAlign.Start,
            color = textColors.dark
        )


        AnimatedVisibility(
            !showNotice
        ) {
            Row(
                modifier = Modifier
                    .size(710.px.dp, 150.px.dp)
                    .align(Alignment.Start),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = dateTimeUiState.date,
                    fontSize = 75.px.sp,
                    fontFamily = Mulish,
                    color = textColors.dark
                )

                Text(
                    text = dateTimeUiState.weekday,
                    fontSize = 75.px.sp,
                    fontFamily = Mulish,
                    color = textColors.dark
                )
            }
        }

    }

}

