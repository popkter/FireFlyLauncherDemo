package com.pop.fireflydeskdemo.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pop.fireflydeskdemo.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DockViewModel : ViewModel() {

    companion object {

        internal const val DOCK_ICON = "dock_icon"
        private const val DOCK_HOME = "home"
        private const val DOCK_SETTING = "setting"
        private const val DOCK_MUSIC = "music"
        private const val DOCK_NAVI = "navi"
        private const val DOCK_FAN = "fan"
        private const val DOCK_360 = "360"

        val dockIcons = listOf(
            DockInfo(DOCK_HOME, R.drawable.icon_dock_home),
            DockInfo(DOCK_SETTING, R.drawable.icon_dock_suzuki),
            DockInfo(DOCK_MUSIC, R.drawable.icon_dock_music),
            DockInfo(DOCK_NAVI, R.drawable.icon_dock_navi),
            DockInfo(DOCK_FAN, R.drawable.icon_dock_fan),
            DockInfo(DOCK_360, R.drawable.icon_dock_360),
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
        val dockInfo: DockInfo
    )

    data class DockInfo(
        val desc: String,
        val iconRes: Int,
    )
}