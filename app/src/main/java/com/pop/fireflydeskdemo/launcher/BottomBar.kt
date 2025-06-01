package com.pop.fireflydeskdemo.launcher


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.pop.fireflydeskdemo.R
import com.pop.fireflydeskdemo.ext.RouteConfig
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.routeTo
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    dockViewModel: DockViewModel = hiltViewModel<DockViewModel>(),
    onRobotVisibleChanged: () -> Unit = {}
) {

    val dockIconsUiState by dockViewModel.dockProfileState.collectAsStateWithLifecycle()

    val fireFlyColors = LocalFireFlyColors.current


    Row(modifier) {
        LazyRow(
            modifier = Modifier
                .wrapContentWidth()
                .height(200.px.dp)
                .background(color = fireFlyColors.light, shape = MaterialTheme.shapes.large)
                .padding(horizontal = 50.px.dp),
//                .align(Alignment.TopEnd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(80.px.dp),
        ) {

            items(dockIconsUiState) { dockIconsUiState ->
                Icon(
                    painter = painterResource(dockIconsUiState.iconRes),
                    contentDescription = dockIconsUiState.desc.toString(),
                    tint = when (dockIconsUiState.desc) {
                        DockViewModel.DockIconType.Camera360 -> fireFlyColors.night
                        DockViewModel.DockIconType.Fan -> fireFlyColors.orange
                        DockViewModel.DockIconType.Home -> fireFlyColors.blueSea
                        DockViewModel.DockIconType.Map -> fireFlyColors.blueSea
                        DockViewModel.DockIconType.Music -> fireFlyColors.rose
                        DockViewModel.DockIconType.Setting -> fireFlyColors.graySky
                    },
                    modifier = Modifier
                        .size(120.px.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    if (dockIconsUiState.desc == DockViewModel.DockIconType.Home) {
                                        onRobotVisibleChanged.invoke()
                                    }
                                },
                                onTap = {
                                    when (dockIconsUiState.desc) {
                                        DockViewModel.DockIconType.Camera360 -> {}
                                        DockViewModel.DockIconType.Fan -> {}

                                        DockViewModel.DockIconType.Home -> {
                                            navController.routeTo(RouteConfig.Launcher) {
                                                popUpTo(RouteConfig.Launcher) {
                                                    saveState =
                                                        true          // 保存目标 destination 的状态
                                                }
                                                launchSingleTop =
                                                    true       // 避免多次实例化同一个 destination
                                                restoreState = true          // 恢复先前保存的状态
                                            }
                                        }

                                        DockViewModel.DockIconType.Map -> {
                                            navController.routeTo(RouteConfig.Navigation) {
                                                popUpTo(RouteConfig.Launcher) {
                                                    saveState =
                                                        true          // 保存目标 destination 的状态
                                                }
                                                launchSingleTop =
                                                    true       // 避免多次实例化同一个 destination
                                                restoreState = true          // 恢复先前保存的状态
                                            }
                                        }

                                        DockViewModel.DockIconType.Music -> {}

                                        DockViewModel.DockIconType.Setting -> {}
                                    }
                                }
                            )
                        }
                )
            }
        }
    }
}

@HiltViewModel
class DockViewModel @Inject constructor() : ViewModel() {

    companion object {

        val _dockProfile = listOf(
            DockUiState(DockIconType.Home, R.drawable.icon_dock_home),
            DockUiState(DockIconType.Setting, R.drawable.icon_dock_suzuki),
            DockUiState(DockIconType.Music, R.drawable.icon_dock_music),
            DockUiState(DockIconType.Map, R.drawable.icon_dock_navi),
            DockUiState(DockIconType.Fan, R.drawable.icon_dock_fan),
            DockUiState(DockIconType.Camera360, R.drawable.icon_dock_360),
        )
    }


    private val _dockProfileState = MutableStateFlow<List<DockUiState>>(emptyList())
    val dockProfileState = _dockProfileState.asStateFlow()

    private val _dockTypeState = MutableStateFlow<DockIconType>(DockIconType.Home)
    val dockTypeState = _dockTypeState.asStateFlow()

    init {
        viewModelScope.launch {
            _dockProfileState.emit(_dockProfile)
        }
    }

    fun updateDock(dockType: DockIconType) {
        _dockTypeState.value = if (dockType == _dockTypeState.value) DockIconType.Home else dockType
    }

    data class DockUiState(
        val desc: DockIconType,
        val iconRes: Int,
    )

    sealed class DockIconType {
        data object Home : DockIconType()
        data object Setting : DockIconType()
        data object Music : DockIconType()
        data object Map : DockIconType()
        data object Fan : DockIconType()
        data object Camera360 : DockIconType()
    }
}