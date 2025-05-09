package com.pop.fireflydeskdemo.ui.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import com.pop.fireflydeskdemo.ui.second_component.DriveModeComponent
import com.pop.fireflydeskdemo.ui.second_component.MusicComponent
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors
import com.pop.fireflydeskdemo.ui.theme.LocalTextColors
import com.pop.fireflydeskdemo.vm.DriveModeViewModel
import com.pop.fireflydeskdemo.vm.MusicViewModel

@Composable
fun SecondComponent(
    modifier: Modifier = Modifier,
    musicViewModel: MusicViewModel,
    driveModeViewModel: DriveModeViewModel
) {

    var scaled by remember { mutableStateOf(false) }

    val fireFlyColors = LocalFireFlyColors.current

    val textColors = LocalTextColors.current

    // 动画控制缩放系数
    val scale by animateFloatAsState(
        targetValue = if (scaled) 0.8f else 1f,
        animationSpec = tween(durationMillis = 500),
        label = "scale"
    )


    Box(
        modifier = modifier
            .graphicsLayer {
                // 设置缩放比例
                scaleX = scale
                scaleY = scale

                // 设置缩放锚点为左下角
                transformOrigin = TransformOrigin(0f, 1f)
            }
            .clip(RoundedCornerShape(50)),
//            .clickable { scaled = !scaled } // 点击触发缩放
        contentAlignment = Alignment.Center
    ) {

        val pagerState = rememberPagerState { 2 }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) {
            when (it) {
                0 -> MusicComponent(musicViewModel)
//                1 -> DriveModeComponent()
                1 -> DriveModeComponent(driveModeViewModel)
            }
        }
    }
}
