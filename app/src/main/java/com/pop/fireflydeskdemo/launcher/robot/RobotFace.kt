package com.pop.fireflydeskdemo.launcher.robot

import android.view.View
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.popkter.robot.Robot
import com.popkter.robot.status.Focus
import com.popkter.robot.status.Happiness
import com.popkter.robot.status.Ordinary
import com.popkter.robot.viewmodel.RobotViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@Composable
fun RobotFace(
    robotViewModel: RobotViewModel = hiltViewModel<RobotViewModel>()
) {
    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        robotViewModel.updateStatus(Ordinary)
                    },
                    onDoubleTap = {
                        robotViewModel.updateStatus(Focus)
                    },
                    onTap = {
                        robotViewModel.updateStatus(Happiness)
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Robot(viewModel = robotViewModel)
    }
}


@HiltViewModel
class RobotFaceViewModel @Inject constructor() : ViewModel() {

    private val _robotVisibleState = MutableStateFlow(false)
    val robotVisibleState = _robotVisibleState.asStateFlow()


    fun triggerVisible() {
        _robotVisibleState.value = !_robotVisibleState.value
    }

}