package com.pop.fireflydeskdemo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pop.fireflydeskdemo.ext.RouteConfig
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.routeTo
import com.pop.fireflydeskdemo.launcher.DockViewModel
import com.pop.fireflydeskdemo.launcher.PopLauncher
import com.pop.fireflydeskdemo.launcher.robot.RobotFace
import com.pop.fireflydeskdemo.launcher.robot.RobotFaceViewModel
import com.pop.fireflydeskdemo.map.PopNavi
import com.pop.fireflydeskdemo.ui.theme.AppTheme
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors
import com.popkter.robot.viewmodel.RobotViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.selects.select

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val robotViewModel by viewModels<RobotViewModel>()
    private val robotFaceViewModel by viewModels<RobotFaceViewModel>()

    private val dockViewModel by viewModels<DockViewModel>()

    @SuppressLint("UnusedBoxWithConstraintsScope")
    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        hideSystemUI()
        setContent {

            AppTheme {
                val navController = rememberNavController()
                val fireFlyColors = LocalFireFlyColors.current
                val robotVisible by robotFaceViewModel.robotVisibleState.collectAsStateWithLifecycle()
                val docksProfile by dockViewModel.dockProfileState.collectAsStateWithLifecycle()
                val dockTypeState by dockViewModel.dockTypeState.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    snapshotFlow { dockTypeState }.collectLatest {
                        when (it) {
                            DockViewModel.DockIconType.Camera360 -> {}
                            DockViewModel.DockIconType.Fan -> {}

                            DockViewModel.DockIconType.Home -> {
                                navController.routeTo(RouteConfig.Launcher) {
                                    popUpTo(RouteConfig.Launcher) {
                                        saveState = true          // 保存目标 destination 的状态
                                    }
                                    launchSingleTop = true       // 避免多次实例化同一个 destination
                                    restoreState = true          // 恢复先前保存的状态
                                }
                            }

                            DockViewModel.DockIconType.Map -> {
                                navController.routeTo(RouteConfig.Navigation) {
                                    popUpTo(RouteConfig.Launcher) {
                                        saveState = true          // 保存目标 destination 的状态
                                    }
                                    launchSingleTop = true       // 避免多次实例化同一个 destination
                                    restoreState = true          // 恢复先前保存的状态
                                }
                            }

                            DockViewModel.DockIconType.Music -> {}

                            DockViewModel.DockIconType.Setting -> {}
                        }
                    }
                }

                SharedTransitionLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(fireFlyColors.lime)
                ) {

                    NavHost(
                        navController = navController,
                        startDestination = RouteConfig.Launcher,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        composable(
                            route = RouteConfig.Launcher,
                            enterTransition = {
                                scaleIn(transformOrigin = TransformOrigin(0.025F, 0.9F))
                            },
                            exitTransition = {
                                scaleOut(transformOrigin = TransformOrigin(0.025F, 0.9F))
                            }
                        ) {
                            PopLauncher(
                                naviToMap = {
                                    navController.routeTo(RouteConfig.Navigation) {
                                        popUpTo(RouteConfig.Launcher) {
                                            saveState = true          // 保存目标 destination 的状态
                                        }
                                        launchSingleTop = true       // 避免多次实例化同一个 destination
                                        restoreState = true          // 恢复先前保存的状态
                                    }
                                },
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedContentScope = this@composable
                            )
                        }

                        composable(
                            route = RouteConfig.Navigation,
                            enterTransition = {
                                scaleIn(transformOrigin = TransformOrigin(0.1F, 0.9F))
                            },
                            exitTransition = {
                                scaleOut(transformOrigin = TransformOrigin(0.1F, 0.9F))
                            }
                        ) {
                            PopNavi(
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedContentScope = this@composable
                            )
                        }
                    }


                }

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {

                    NavigationBar(
                        modifier = Modifier
                            .padding(50.px.dp)
                            .align(Alignment.BottomStart)
                            .clip(MaterialTheme.shapes.large),
                        containerColor = fireFlyColors.grape
                    ) {
                        docksProfile.onEach { (dockType, iconRes) ->
                            NavigationBarItem(
                                modifier = Modifier
                                    .size(120.px.dp),
                                icon = {
                                    Icon(
                                        painter = painterResource(iconRes),
                                        contentDescription = dockType.toString(),
                                        tint = if (dockType == dockTypeState) {
                                            fireFlyColors.lime
                                        } else {
                                            when (dockType) {
                                                DockViewModel.DockIconType.Camera360 -> fireFlyColors.night
                                                DockViewModel.DockIconType.Fan -> fireFlyColors.orange
                                                DockViewModel.DockIconType.Home -> fireFlyColors.blueSea
                                                DockViewModel.DockIconType.Map -> fireFlyColors.blueSea
                                                DockViewModel.DockIconType.Music -> fireFlyColors.rose
                                                DockViewModel.DockIconType.Setting -> fireFlyColors.darkLoam
                                            }
                                        },
                                    )
                                },
                                selected = /*dockType == dockTypeState*/ false,
                                onClick = {
                                    dockViewModel.updateDock(dockType)
                                },
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = robotVisible,
                        modifier =
                            Modifier
                                .padding(end = 50.px.dp, bottom = 50.px.dp)
                                .size(200.px.dp)
                                .align(Alignment.BottomEnd),
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()

                    ) {
                        BoxWithConstraints(
                            modifier = Modifier
                                .size(200.px.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .then(Modifier.layout { measurable, _ ->
                                        val placeable = measurable.measure(Constraints())
                                        layout(placeable.width, placeable.height) {
                                            placeable.place(0, 0)
                                        }
                                    })
                                    .size(200.dp, 200.dp)
                                    .scale(.4F)
                            ) {
                                RobotFace(robotViewModel = robotViewModel)
                            }
                        }
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