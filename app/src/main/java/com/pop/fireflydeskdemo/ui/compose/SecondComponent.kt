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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.ui.theme.AppTheme
import com.pop.fireflydeskdemo.ui.theme.Mulish

@Composable
fun SecondComponent(modifier: Modifier = Modifier) {

    var scaled by remember { mutableStateOf(false) }

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
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.secondaryContainer),
//            .clickable { scaled = !scaled } // 点击触发缩放
        contentAlignment = Alignment.Center
    ) {


        // TODO: 音乐界面，圆形进度条
        Column(
            modifier = Modifier
//                .background(
//                    Brush.linearGradient(
//                        colors = listOf(Lime, Grape)
//                    )
//                )
                .fillMaxSize()
                .padding(100.px.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "不眠之夜",
                    fontSize = 100.px.sp,
                    fontFamily = Mulish,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "张杰",
                    fontSize = 60.px.sp,
                    fontFamily = Mulish,
                    color = Color.White,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.CenterHorizontally)
                )
            }


            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(1F),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    contentDescription = "",
                    modifier = Modifier.size(240.px.dp),
                )
                Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    contentDescription = "",
                    modifier = Modifier.size(240.px.dp)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    contentDescription = "",
                    modifier = Modifier.size(240.px.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "别再沉醉 别再枯萎",
                    fontSize = 50.px.sp,
                    fontFamily = Mulish,
                    color = Color.White,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.CenterHorizontally)
                        .alpha(.8F)
                        .scale(.8F)
                )

                Text(
                    text = "继续沉醉 自我迂回",
                    fontSize = 50.px.sp,
                    fontFamily = Mulish,
                    color = Color.White,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "最后品味 永恒的滋味",
                    fontSize = 50.px.sp,
                    fontFamily = Mulish,
                    color = Color.White,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.CenterHorizontally)
                        .alpha(.8F)
                        .scale(.8F)
                )
            }


        }


    }
}


@Preview(widthDp = 978, heightDp = 978)
@Composable
fun SecondComponentPreview() {
    AppTheme {
        SecondComponent()
    }
}