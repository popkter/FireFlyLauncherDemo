package com.pop.fireflydeskdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pop.fireflydeskdemo.ext.RouteConfig
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.routeTo
import com.pop.fireflydeskdemo.launcher.BottomBar
import com.pop.fireflydeskdemo.launcher.PopLauncher
import com.pop.fireflydeskdemo.map.PopNavi
import com.pop.fireflydeskdemo.ui.theme.AppTheme
import com.popkter.robot.viewmodel.RobotViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val robotViewModel by viewModels<RobotViewModel>()

    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        hideSystemUI()
        setContent {

            AppTheme {
                val navController = rememberNavController()

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

                    BottomBar(
                        Modifier
                            .offset(x = 50.px.dp, y = -50.px.dp)
                            .align(Alignment.BottomStart),
                        navController,
                        robotViewModel
                    )
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