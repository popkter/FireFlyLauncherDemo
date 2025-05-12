package com.pop.fireflydeskdemo.map

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.pop.fireflydeskdemo.R
import com.pop.fireflydeskdemo.ui.theme.AppTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PopNavi(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {

    with(sharedTransitionScope) {
        Box(
            modifier = Modifier
                .sharedElement(
                    sharedContentState = sharedTransitionScope.rememberSharedContentState(key = "map"),
                    animatedVisibilityScope = animatedContentScope
                )
                .fillMaxSize()
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(R.drawable.map_capture),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }
    }

}


@Composable
@Preview
fun PopNaviPreview() {
    AppTheme {
//        PopNavi()
    }
}