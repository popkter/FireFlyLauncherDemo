package com.pop.fireflydeskdemo.ui.second_component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.pop.fireflydeskdemo.R
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors
import com.pop.fireflydeskdemo.ui.theme.LocalTextColors
import com.pop.fireflydeskdemo.ui.theme.Mulish
import com.pop.fireflydeskdemo.vm.MusicViewModel

@Composable
fun MusicComponent(
    musicViewModel: MusicViewModel
) {

    val fireFlyColors = LocalFireFlyColors.current
    val textColors = LocalTextColors.current

    Box(
        modifier = Modifier
            .background(fireFlyColors.grape)
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .padding(top = 150.px.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = "不眠之夜",
                fontSize = 120.px.sp,
                lineHeight = 120.px.sp,
                fontFamily = Mulish,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                color = textColors.light,
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = "张杰",
                fontSize = 60.px.sp,
                fontFamily = Mulish,
                color = textColors.light,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Thin,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally)
            )
        }

        Row(
            modifier = Modifier
                .size(720.px.dp, 240.px.dp)
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_pre_song),
                tint = fireFlyColors.light,
                contentDescription = "",
                modifier = Modifier.size(140.px.dp),
            )
            Icon(
                painter = painterResource(R.drawable.icon_pause_song),
                tint = fireFlyColors.light,
                contentDescription = "",
                modifier = Modifier.size(140.px.dp)
            )
            Icon(
                painter = painterResource(R.drawable.icon_next_song),
                tint = fireFlyColors.light,
                contentDescription = "",
                modifier = Modifier.size(140.px.dp)
            )
        }

        CircularProgressIndicator(
            progress = { .75F },
            modifier = Modifier
                .padding(20.px.dp)
                .fillMaxSize(),
            strokeWidth = 50.px.dp,
            color = fireFlyColors.yellow,
            trackColor = Color.Transparent
        )


        /*           Column(
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
                   }*/
    }
}