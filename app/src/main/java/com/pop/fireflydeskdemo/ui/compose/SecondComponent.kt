package com.pop.fireflydeskdemo.ui.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ui.theme.FireFlyDeskDemoTheme
import com.pop.fireflydeskdemo.ui.theme.Grape

@Composable
fun SecondComponent(modifier: Modifier = Modifier) {
    val boxSize = 1000.px.dp

    var scaled by remember { mutableStateOf(false) }

    // 动画控制缩放系数
    val scale by animateFloatAsState(
        targetValue = if (scaled) 0.8f else 1f,
        animationSpec = tween(durationMillis = 500),
        label = "scale"
    )

    val radius by remember { mutableStateOf(boxSize * scale / 2F) }

    Box(
        modifier = modifier
            .graphicsLayer {
                // 设置缩放比例
                scaleX = scale
                scaleY = scale

                // 设置缩放锚点为左下角
                transformOrigin = TransformOrigin(0f, 1f)
            }
            .size(boxSize)
            .background(Grape, RoundedCornerShape(radius))
            .clickable { scaled = !scaled } // 点击触发缩放
    ) {

    }
}


@Preview(showBackground = true)
@Composable
fun SecondComponentPreview() {
    FireFlyDeskDemoTheme {
        SecondComponent()
    }
}