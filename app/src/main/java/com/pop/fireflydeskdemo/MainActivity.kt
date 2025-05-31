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
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.layout
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
import com.pop.fireflydeskdemo.launcher.BottomBar
import com.pop.fireflydeskdemo.launcher.PopLauncher
import com.pop.fireflydeskdemo.launcher.robot.RobotFace
import com.pop.fireflydeskdemo.launcher.robot.RobotFaceViewModel
import com.pop.fireflydeskdemo.map.PopNavi
import com.pop.fireflydeskdemo.ui.theme.AppTheme
import com.pop.fireflydeskdemo.ui.theme.shapes
import com.popkter.robot.viewmodel.RobotViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val robotViewModel by viewModels<RobotViewModel>()
    private val robotFaceViewModel by viewModels<RobotFaceViewModel>()

    @SuppressLint("UnusedBoxWithConstraintsScope")
    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        hideSystemUI()
        setContent {

            AppTheme {
                val navController = rememberNavController()
                val robotVisible by robotFaceViewModel.robotVisibleState.collectAsStateWithLifecycle()

                SharedTransitionLayout(
                    modifier = Modifier
                        .fillMaxSize()
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

                    // TODO: Use NavigationBar
                    BottomBar(
                        Modifier
                            .offset(x = 50.px.dp, y = -50.px.dp)
                            .align(Alignment.BottomStart),
                        navController
                    ) {
                        robotFaceViewModel.triggerVisible()
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
                        FloatingActionButton(
                            onClick = {},
                            modifier = Modifier
                                .padding(0.dp),
                            shape = RoundedCornerShape(50),
                            containerColor = Color.Transparent,
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                hoveredElevation = 0.dp,
                                focusedElevation = 0.dp
                            )
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