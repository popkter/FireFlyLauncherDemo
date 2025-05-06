package com.pop.fireflydeskdemo.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.ui.theme.AppTheme
import com.pop.fireflydeskdemo.ui.theme.Mulish
import com.pop.fireflydeskdemo.vm.WeatherUiStateSample
import com.pop.fireflydeskdemo.vm.WeatherViewModel

@Composable
fun RealTimeWeather(
    modifier: Modifier = Modifier,
    weatherUiState: WeatherViewModel.WeatherUiState
) {


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.extraLarge)
    ) {
        AnimatedContent(
            targetState = weatherUiState.iconRes,
            modifier = Modifier
                .size(1500.px.dp)
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.shapes.extraLarge
                )
                .align(Alignment.Center),
            transitionSpec = {
                // 定义进入和退出动画
                (slideInVertically { height -> -height } + fadeIn()).togetherWith(
                    slideOutVertically { height -> -height } + fadeOut())
            }
        ) { res ->

            Box(
                Modifier.fillMaxSize()

            ) {
                Icon(
                    painter = painterResource(res),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier
                        .offset(x = 250.px.dp, y = 310.px.dp)
                        .size(500.px.dp)
                )
            }
        }

        AnimatedContent(
            targetState = weatherUiState.temp,
            transitionSpec = {
                // 定义进入和退出动画
                (slideInVertically { height -> -height } + fadeIn()).togetherWith(slideOutVertically { height -> height } + fadeOut())
            },
            modifier = Modifier
                .padding(start = 582.px.dp, bottom = 540.px.dp)
                .align(Alignment.BottomStart)
        ) { temp ->

            //温度
            Text(
                text = "${temp}℃",
                fontSize = 180.px.sp,
                fontFamily = Mulish,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onPrimary,
            )

        }



        AnimatedContent(
            targetState = weatherUiState.desc,
            transitionSpec = {
                // 定义进入和退出动画
                (slideInVertically { height -> -height } + fadeIn()).togetherWith(slideOutVertically { height -> height } + fadeOut())
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 780.px.dp, end = 420.px.dp)
        ) { desc ->

            //描述
            Text(
                text = desc,
                fontSize = 240.px.sp,
                fontFamily = Mulish,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}


@Composable
@Preview(
    widthDp = 978, heightDp = 978
)
fun RealTimeWeatherPreview() {
    AppTheme {
        Surface {
            Box {
                RealTimeWeather(weatherUiState = WeatherUiStateSample)
            }
        }
    }
}