package com.pop.fireflydeskdemo.ui.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.hilt.navigation.compose.hiltViewModel
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ui.main_component.DateViewModel
import com.pop.fireflydeskdemo.ui.main_component.MemoViewModel
import com.pop.fireflydeskdemo.ui.main_component.NaviViewModel
import com.pop.fireflydeskdemo.ui.main_component.WeatherViewModel
import com.pop.fireflydeskdemo.ui.second_component.DriveModeViewModel
import com.pop.fireflydeskdemo.ui.theme.AppTheme
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors
import com.pop.fireflydeskdemo.vm.MusicViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PopLauncher(
    dateViewModel: DateViewModel = hiltViewModel<DateViewModel>(),
    weatherViewModel: WeatherViewModel = hiltViewModel<WeatherViewModel>(),
    memoViewModel: MemoViewModel = hiltViewModel<MemoViewModel>(),
    naviViewModel: NaviViewModel = hiltViewModel<NaviViewModel>(),
    dockViewModel: DockViewModel = hiltViewModel<DockViewModel>(),
    musicViewModel: MusicViewModel = hiltViewModel<MusicViewModel>(),
    driveModeViewModel: DriveModeViewModel = hiltViewModel<DriveModeViewModel>()
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
                naviViewModel = naviViewModel,
                dateViewModel = dateViewModel,
                weatherViewModel = weatherViewModel,
                memoViewModel = memoViewModel
            )
        }

        TopBar(
            Modifier
                .padding(top = 50.px.dp, start = 50.px.dp)
                .widthIn(710.px.dp, 1910.px.dp)
                .align(Alignment.TopStart),
            dateViewModel
        )

        SecondComponent(
            Modifier
                .padding(start = 50.px.dp, bottom = 300.px.dp)
                .size(1000.px.dp)
                .align(Alignment.BottomStart),
            musicViewModel,
            driveModeViewModel
        )

        BottomBar(
            Modifier
                .padding(start = 50.px.dp, bottom = 50.px.dp)
                .align(Alignment.BottomStart)
                .clickable {
//                                    driveModeViewModel.randomMode()
                    weatherViewModel.updateWeather()
                },
            dockViewModel
        )

    }
}


@Composable
@Preview(widthDp = 1264, heightDp = 790)
fun PopLauncherPreview() {
    AppTheme {
        PopLauncher()
    }
}