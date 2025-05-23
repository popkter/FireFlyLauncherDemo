package com.pop.fireflydeskdemo.launcher


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    dockViewModel: DockViewModel = hiltViewModel<DockViewModel>(),
) {

    val dockIconsUiState by dockViewModel.dockIconsUiState.collectAsStateWithLifecycle()

    val fireFlyColors = LocalFireFlyColors.current

    Box(modifier) {
        LazyRow(
            modifier = Modifier
                .wrapContentWidth()
                .height(200.px.dp)
                .background(color = fireFlyColors.light, shape = MaterialTheme.shapes.large)
                .padding(horizontal = 50.px.dp)
                .align(Alignment.TopEnd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(80.px.dp),
        ) {

            items(dockIconsUiState) {
                Icon(
                    painter = painterResource(it.dockInfo.iconRes),
                    contentDescription = it.dockInfo.desc.toString(),
                    tint = when (it.dockInfo.desc) {
                        DockViewModel.DockIconType.Camera360 -> fireFlyColors.night
                        DockViewModel.DockIconType.Fan -> fireFlyColors.orange
                        DockViewModel.DockIconType.Home -> fireFlyColors.blueSea
                        DockViewModel.DockIconType.Map -> fireFlyColors.blueSea
                        DockViewModel.DockIconType.Music -> fireFlyColors.rose
                        DockViewModel.DockIconType.Setting -> fireFlyColors.graySky
                    },
                    modifier = Modifier
                        .size(120.px.dp)
                        .clickable {
                            when (it.dockInfo.desc) {
                                DockViewModel.DockIconType.Camera360 -> {}
                                DockViewModel.DockIconType.Fan -> {}

                                DockViewModel.DockIconType.Home -> {
                                    navController.routeTo(RouteConfig.Launcher){
                                        popUpTo(RouteConfig.Launcher) {
                                            saveState = true          // 保存目标 destination 的状态
                                        }
                                        launchSingleTop = true       // 避免多次实例化同一个 destination
                                        restoreState = true          // 恢复先前保存的状态
                                    }
                                }

                                DockViewModel.DockIconType.Map -> {
                                    navController.routeTo(RouteConfig.Navigation){
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
                )
            }
        }
    }
}

@HiltViewModel
class DockViewModel @Inject constructor() : ViewModel() {

    companion object {

        val dockIcons = listOf(
            DockInfo(DockIconType.Home, R.drawable.icon_dock_home),
            DockInfo(DockIconType.Setting, R.drawable.icon_dock_suzuki),
            DockInfo(DockIconType.Music, R.drawable.icon_dock_music),
            DockInfo(DockIconType.Map, R.drawable.icon_dock_navi),
            DockInfo(DockIconType.Fan, R.drawable.icon_dock_fan),
            DockInfo(DockIconType.Camera360, R.drawable.icon_dock_360),
        )
    }


    private val _dockIconsUiState = MutableStateFlow<List<DockUiState>>(emptyList())
    val dockIconsUiState = _dockIconsUiState.asStateFlow()

    init {
        viewModelScope.launch {
            _dockIconsUiState.emit(dockIcons.map { info ->
                DockUiState(info)
            })
        }
    }

    data class DockUiState(
        val dockInfo: DockInfo,
    )

    sealed class DockIconType {
        data object Home : DockIconType()
        data object Setting : DockIconType()
        data object Music : DockIconType()
        data object Map : DockIconType()
        data object Fan : DockIconType()
        data object Camera360 : DockIconType()
    }

    data class DockInfo(
        val desc: DockIconType,
        val iconRes: Int,
    )
}