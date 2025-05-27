package com.pop.fireflydeskdemo.launcher.robot

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.hilt.navigation.compose.hiltViewModel
import com.popkter.robot.Robot
import com.popkter.robot.status.Focus
import com.popkter.robot.status.Happiness
import com.popkter.robot.status.Ordinary
import com.popkter.robot.viewmodel.RobotViewModel

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


//@Composable
//fun UnityView() {
//
//    AndroidView(
//        factory = {
//            val unityPlayer = UnityPlayer(it)
//            unityPlayer.view
//        },
//        modifier = Modifier.fillMaxSize()
//    )
//}