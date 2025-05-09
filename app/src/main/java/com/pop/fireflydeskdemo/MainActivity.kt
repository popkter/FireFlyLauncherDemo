package com.pop.fireflydeskdemo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ui.compose.BottomBar
import com.pop.fireflydeskdemo.ui.compose.DockViewModel
import com.pop.fireflydeskdemo.ui.compose.MainComponent
import com.pop.fireflydeskdemo.ui.compose.SecondComponent
import com.pop.fireflydeskdemo.ui.compose.TopBar
import com.pop.fireflydeskdemo.ui.main_component.DateViewModel
import com.pop.fireflydeskdemo.ui.main_component.MemoViewModel
import com.pop.fireflydeskdemo.ui.main_component.NaviViewModel
import com.pop.fireflydeskdemo.ui.main_component.WeatherViewModel
import com.pop.fireflydeskdemo.ui.second_component.DriveModeViewModel
import com.pop.fireflydeskdemo.ui.theme.AppTheme
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors
import com.pop.fireflydeskdemo.vm.MusicViewModel

class MainActivity : ComponentActivity() {
    private val naviViewModel by viewModels<NaviViewModel>()
    private val dateViewModel by viewModels<DateViewModel>()
    private val weatherViewModel by viewModels<WeatherViewModel>()
    private val memoViewModel by viewModels<MemoViewModel>()
    private val musicViewModel by viewModels<MusicViewModel>()
    private val driveModeViewModel by viewModels<DriveModeViewModel>()
    private val dockViewModel by viewModels<DockViewModel>()

    @SuppressLint("UnusedBoxWithConstraintsScope")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        hideSystemUI()
        setContent {
            AppTheme {
                Surface {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                    )

                    val colorScheme = LocalFireFlyColors.current

                    BoxWithConstraints(
                        modifier = Modifier
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
                                    driveModeViewModel.randomMode()
//                                    weatherViewModel.updateWeather()
                                },
                            dockViewModel
                        )

                    }
                }
            }
        }
    }


    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController.apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}