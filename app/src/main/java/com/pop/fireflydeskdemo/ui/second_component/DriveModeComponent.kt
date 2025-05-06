package com.pop.fireflydeskdemo.ui.second_component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors


@Composable
fun DriveModeComponent() {

    val pagerState = rememberPagerState { 4 }

    val fireFlyColors = LocalFireFlyColors.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fireFlyColors.blueSky),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.px.dp)
                .background(Color.Black),
            pageSize = PageSize.Fixed(333.px.dp),
            state = pagerState
        ) { page ->
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Red),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "第${page}页"
                )
            }
        }
    }

}