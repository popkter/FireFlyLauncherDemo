package com.pop.fireflydeskdemo.launcher.map

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.pop.fireflydeskdemo.R
import com.pop.fireflydeskdemo.vm.base.MainComponentController
import com.pop.fireflydeskdemo.vm.base.MainComponentViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NaviComponent(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    naviToMap: () -> Unit,
) {

    with(sharedTransitionScope) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(50))
                .sharedElement(
                    sharedContentState = sharedTransitionScope.rememberSharedContentState("map"),
                    animatedVisibilityScope = animatedContentScope,
                    clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(50))
                )
                .clickable {
                    naviToMap()
                }) {
            Image(
                modifier = modifier,
                painter = painterResource(id = R.drawable.map_capture),
                contentDescription = "",
                contentScale = ContentScale.FillWidth
            )
        }

    }
}

@HiltViewModel
class NaviViewModel @Inject constructor() : MainComponentViewModel() {

    companion object {
        private const val TO_HOME = "to_home"
        private const val TO_WORK = "to_work"
        private const val TO_FAVORITE = "to_favorite"
        private const val TO_SEARCH = "to_search"
    }

    override val controller = listOf(
        MainComponentController(
            TO_HOME,
            R.drawable.to_home
        ),
        MainComponentController(
            TO_WORK,
            R.drawable.to_work
        ),
        MainComponentController(
            TO_FAVORITE,
            R.drawable.to_favorite
        ),
        MainComponentController(
            TO_SEARCH,
            R.drawable.to_search
        )
    )


    override fun onControllerClick(controller: MainComponentController) {
        when (controller.desc) {
            TO_HOME -> {

            }

            TO_WORK -> {

            }

            TO_FAVORITE -> {

            }

            TO_SEARCH -> {

            }
        }
    }

}