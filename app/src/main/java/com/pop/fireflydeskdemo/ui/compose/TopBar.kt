package com.pop.fireflydeskdemo.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.ui.theme.Cloud
import com.pop.fireflydeskdemo.ui.theme.FireFlyDeskDemoTheme
import com.pop.fireflydeskdemo.ui.theme.Monoton
import com.pop.fireflydeskdemo.ui.theme.Mulish
import com.pop.fireflydeskdemo.ui.theme.Night
import com.pop.fireflydeskdemo.ui.theme.Sea
import com.pop.fireflydeskdemo.ui.theme.Stone
import com.pop.fireflydeskdemo.ui.theme.componentRadius
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TopBar(modifier: Modifier = Modifier) {
    var showNotice by remember { mutableStateOf(false) }

    // 动画控制缩放系数
    val scale by animateFloatAsState(
        targetValue = if (showNotice) 0.5f else 1f,
        animationSpec = tween(durationMillis = 200, easing = LinearEasing),
        label = "scale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (showNotice) 0F else 1F,
        animationSpec = tween(durationMillis = 200, easing = LinearEasing),
        label = "alpha"
    )


    val dateTime by produceState(initialValue = LocalDateTime.now()) {
        while (true) {
            value = LocalDateTime.now()
            delay(1000L)
        }
    }

    val timeStr = dateTime.format(DateTimeFormatter.ofPattern("HH : mm"))
    val monthDayStr = "${dateTime.monthValue}月${dateTime.dayOfMonth}日"
    val weekdayStr = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA) // "星期二"


    Column(modifier = modifier) {

        AnimatedVisibility(
            showNotice
        ) {
            //notice
            Row(
                modifier = Modifier
                    .height(200.px.dp)
                    .background(color = Sea, shape = RoundedCornerShape(componentRadius))
                    .padding(horizontal = 50.px.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    tint = Cloud,
                    contentDescription = "",
                    modifier = Modifier.size(90.px.dp),
                )

                Text(
                    text = "测试文本，用于测试通知是否生效",
                    maxLines = 1,
                    fontFamily = Mulish,
                    fontSize = 60.px.sp,
                    color = Cloud,
                    modifier = Modifier
                        .padding(start = 50.px.dp)
                        .wrapContentWidth(),
                )
            }
        }


        //clock
        Box(
            modifier = Modifier
                .graphicsLayer {
                    // 设置缩放比例
                    scaleX = scale
                    scaleY = scale
                    // 设置缩放锚点为左下角
                    transformOrigin = TransformOrigin(0f, 0.5f)
                }
                .width(710.px.dp)
                .height(400.px.dp)
                .clickable(true) {
                    showNotice = !showNotice
                },
        ) {
            Text(
                modifier = Modifier
                    .size(710.px.dp, 240.px.dp)
                    .align(Alignment.TopStart),
                text = timeStr,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Visible,
                fontFamily = Monoton,
                fontSize = 240.px.sp,
                color = Night,
                textAlign = TextAlign.Start,
            )

            Text(
                text = monthDayStr,
                fontSize = 75.px.sp,
                fontFamily = Mulish,
                color = Stone,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .alpha(alpha)
            )

            Text(
                text = weekdayStr,
                fontSize = 75.px.sp,
                fontFamily = Mulish,
                color = Stone,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .alpha(alpha)
            )
        }


    }

}

@Preview(showBackground = true)
@Composable
fun TopPreview() {
    FireFlyDeskDemoTheme {
        Column {
            TopBar()
            TopBar()
        }
    }
}