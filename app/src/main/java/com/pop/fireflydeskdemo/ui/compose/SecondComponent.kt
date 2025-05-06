package com.pop.fireflydeskdemo.ui.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.ui.theme.AppTheme
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors
import com.pop.fireflydeskdemo.ui.theme.Mulish
import com.pop.fireflydeskdemo.R
import com.pop.fireflydeskdemo.ui.second_component.DriveModeComponent
import com.pop.fireflydeskdemo.ui.second_component.MusicComponent
import com.pop.fireflydeskdemo.ui.theme.LocalTextColors
import com.pop.fireflydeskdemo.vm.MusicViewModel

@Composable
fun SecondComponent(modifier: Modifier = Modifier, musicViewModel: MusicViewModel) {

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
                1 -> DriveModeComponent()
            }
        }
    }
}
