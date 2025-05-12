package com.pop.fireflydeskdemo.launcher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.pop.fireflydeskdemo.launcher.drivemode.DriveModeComponent
import com.pop.fireflydeskdemo.launcher.drivemode.DriveModeViewModel
import com.pop.fireflydeskdemo.launcher.music.MusicComponent
import com.pop.fireflydeskdemo.ui.ext.InfiniteHorizontalPager
import com.pop.fireflydeskdemo.ui.ext.rememberInfinitePagerState
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors
import com.pop.fireflydeskdemo.vm.MusicViewModel

@Composable
fun SecondComponent(
    modifier: Modifier = Modifier,
    musicViewModel: MusicViewModel,
    driveModeViewModel: DriveModeViewModel,
) {
    val fireFlyColors = LocalFireFlyColors.current

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50)),
        contentAlignment = Alignment.Center
    ) {

        val initialIndex = Int.MAX_VALUE / 2

        val pagerState = rememberInfinitePagerState(initialIndex)

        InfiniteHorizontalPager(
            actualPageCount = 3,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { actualIndex, virtualIndex ->
            when (actualIndex) {
                0 -> MusicComponent(musicViewModel)
                1 -> DriveModeComponent(driveModeViewModel)
                2 -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = fireFlyColors.night,
                            shape = MaterialTheme.shapes.extraLarge
                        )
                )
            }
        }
    }
}
