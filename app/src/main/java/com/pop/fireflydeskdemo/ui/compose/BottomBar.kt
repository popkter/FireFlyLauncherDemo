package com.pop.fireflydeskdemo.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ui.theme.Cloud
import com.pop.fireflydeskdemo.ui.theme.Rose
import com.pop.fireflydeskdemo.ui.theme.Sea
import com.pop.fireflydeskdemo.ui.theme.componentRadius

@Composable
fun BottomBar(modifier: Modifier = Modifier) {
    Box(modifier) {
        LazyRow(
            modifier = Modifier
                .wrapContentWidth()
                .height(200.px.dp)
                .background(color = Cloud, shape = RoundedCornerShape(componentRadius))
                .padding(horizontal = 50.px.dp)
                .align(Alignment.TopEnd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(80.px.dp),
        ) {
            items(10) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "",
                    tint = Sea,
                    modifier = Modifier.size(120.px.dp)
                )
            }
        }
    }
}