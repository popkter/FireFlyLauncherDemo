package com.pop.fireflydeskdemo.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.ui.theme.BlueSea
import com.pop.fireflydeskdemo.ui.theme.FireFlyDeskDemoTheme
import com.pop.fireflydeskdemo.ui.theme.GraySky
import com.pop.fireflydeskdemo.ui.theme.Mulish
import com.pop.fireflydeskdemo.ui.theme.PureWhite
import com.pop.fireflydeskdemo.ui.theme.TiltWrap
import com.pop.fireflydeskdemo.ui.theme.componentRadius
import com.pop.fireflydeskdemo.vm.DateViewModel
import kotlinx.serialization.EncodeDefault

@Composable
fun TopBar(
    modifier: Modifier = Modifier, dateViewModel: DateViewModel,default: Boolean = false
) {

    val dateTimeUiState by dateViewModel.dateTimeUiState.collectAsStateWithLifecycle()

    val dateTimeInfo = dateTimeUiState.dateTimeInfo

    var showNotice by remember { mutableStateOf(default) }

//    val scale by animateFloatAsState(
//        targetValue = if (showNotice) 0.9f else 1f,
//        animationSpec = tween(durationMillis = 200, easing = LinearEasing),
//        label = "scale"
//    )

    Column(modifier = modifier) {

        AnimatedVisibility(
            showNotice
        ) {
            //notice
            Row(
                modifier = Modifier
                    .height(200.px.dp)
                    .background(color = BlueSea, shape = RoundedCornerShape(componentRadius))
                    .padding(horizontal = 50.px.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    tint = PureWhite,
                    contentDescription = "",
                    modifier = Modifier.size(90.px.dp),
                )

                Text(
                    text = "测试文本，用于测试通知是否生效",
                    maxLines = 1,
                    fontFamily = Mulish,
                    fontSize = 60.px.sp,
                    color = PureWhite,
                    modifier = Modifier.wrapContentWidth(),
                )
            }
        }


        Text(
            modifier = Modifier
//                .offset(y = -30.px.dp)
                .wrapContentSize()
                .clickable {
                    showNotice = !showNotice
                }
//                .background(Grape)
//                .graphicsLayer {
                    // 设置缩放比例
//                    scaleX = scale
//                    scaleY = scale
                    // 设置缩放锚点为左下角
//                    transformOrigin = TransformOrigin(0f, 1f)
//                }
            ,
            text = dateTimeInfo.time,
            fontFamily = TiltWrap,
            fontSize = 240.px.sp,
            lineHeight = 240.px.sp,
            textAlign = TextAlign.Start)


        AnimatedVisibility(
            !showNotice
        ) {
            Row(
                modifier = Modifier
                    .size(710.px.dp, 150.px.dp)
                    .align(Alignment.Start),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = dateTimeInfo.date,
                    fontSize = 75.px.sp,
                    fontFamily = Mulish,
                    color = GraySky,
                )

                Text(
                    text = dateTimeInfo.weekday,
                    fontSize = 75.px.sp,
                    fontFamily = Mulish,
                    color = GraySky,
                )
            }
        }

    }
}

@Composable
@Preview(widthDp = 1920, heightDp = 1080)
fun TopBarPreview(){
    FireFlyDeskDemoTheme {
        Row {
            TopBar(dateViewModel = DateViewModel(), default = true)
            TopBar(dateViewModel = DateViewModel(), default = false)
        }
    }
}