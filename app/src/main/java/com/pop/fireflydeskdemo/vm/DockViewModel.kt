package com.pop.fireflydeskdemo.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pop.fireflydeskdemo.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DockViewModel : ViewModel() {

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
        val dockInfo: DockInfo
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