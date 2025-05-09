package com.pop.fireflydeskdemo.ui.main_component

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import com.pop.fireflydeskdemo.R
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.launchOnIO
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.ui.theme.AppTheme
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors
import com.pop.fireflydeskdemo.ui.theme.Mulish
import com.pop.fireflydeskdemo.vm.base.MainComponentController
import com.pop.fireflydeskdemo.vm.base.MainComponentViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MemoComponent(
    modifier: Modifier = Modifier,
    memoUiState: List<MemoDataItem> = memoDataItemSample,
) {

    val list =
        Array(1) { MemoDataItem() }.toList() + memoUiState + Array(3) { MemoDataItem() }.toList()

    val colors = LocalFireFlyColors.current

    val pagerState = rememberPagerState { list.size }


    val scope = rememberCoroutineScope()

    BoxWithConstraints(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(colors.light, MaterialTheme.shapes.extraLarge)
    ) {

        VerticalPager(
            pagerState,
            modifier = Modifier
                .then(Modifier.layout { measurable, _ ->
                    val placeable = measurable.measure(Constraints())
                    layout(placeable.width, placeable.height) {
                        placeable.place(0, 0)
                    }
                })
                .offset(x = -40.px.dp)
                .size(2200.px.dp, 1460.px.dp)
                .align(Alignment.BottomStart),
            pageSize = object : PageSize {
                override fun Density.calculateMainAxisPageSize(
                    availableSpace: Int, pageSpacing: Int,
                ): Int = (availableSpace - 2 * pageSpacing) / 5
            },
            snapPosition = SnapPosition.Center
        ) { index ->

            val pageOffset =
                ((pagerState.currentPage - index - 1) + pagerState.currentPageOffsetFraction).coerceIn(
                    -1f, 1f
                )

            // 缩放因子，越靠近当前页 scale 越大
            val scale = 0.9f + (1 - abs(pageOffset)) * 0.1f
            val alpha = 0.7f + (1 - abs(pageOffset)) * 0.3f


            if (list[index].content.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                        }
                        .fillMaxWidth()
                        .height(240.px.dp)
                        .background(colors.blueSky)
                        .padding(start = 200.px.dp)
                        .clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "$index ${list[index].content}",
                        color = colors.night,
                        fontSize = 60.px.sp,
                        fontFamily = Mulish,
                    )

                }
            } else {
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                        }
                        .fillMaxWidth()
                        .height(240.px.dp)
                ) {}
            }

        }

        Canvas(Modifier.size(2060.px.dp)) {
            drawCircle(
                colors.light,
                style = Stroke(240.px.dp.value)
            )
        }
    }

}


@Composable
@Preview(widthDp = 978, heightDp = 978)
fun MemoComponentPreview() {
    AppTheme {
        MemoComponent(Modifier.fillMaxSize())
    }
}

val memoDataItemSample = listOf(
    MemoDataItem(
        "今天的工作计划是摸鱼"
    ),
    MemoDataItem(
        "今天的工作计划是摸鱼，明天也是"
    ),
    MemoDataItem(
        "今天的工作计划是摸鱼，明天也是，后天也是"
    ),
    MemoDataItem(
        "今天的工作计划是摸鱼，明天也是，后天也是"
    ),
    MemoDataItem(
        "今天的工作计划是摸鱼，明天也是，后天也是"
    ),
    MemoDataItem(
        "今天的工作计划是摸鱼，明天也是，后天也是"
    ),
    MemoDataItem(
        "今天的工作计划是摸鱼，明天也是，后天也是"
    ),
    MemoDataItem(
        "今天的工作计划是摸鱼，明天也是，后天也是"
    ),
    MemoDataItem(
        "今天的工作计划是摸鱼，明天也是，后天也是"
    ),
    MemoDataItem(
        "今天的工作计划是摸鱼，明天也是，后天也是"
    ),
    MemoDataItem(
        "今天的工作计划是摸鱼，明天也是，后天也是"
    ),
    MemoDataItem(
        "今天的工作计划是摸鱼，明天也是，后天也是"
    ),
    MemoDataItem(
        "今天的工作计划是摸鱼，明天也是，后天也是"
    ),
    MemoDataItem(
        "今天的工作计划是摸鱼，明天也是，后天也是"
    ),
    MemoDataItem(
        "今天的工作计划是摸鱼，明天也是，后天也是"
    ),
    MemoDataItem(
        "今天的工作计划是摸鱼，明天也是，后天也是"
    ),
    MemoDataItem(
        "今天的工作计划是摸鱼，明天也是，后天也是"
    )
)


class MemoViewModel : MainComponentViewModel() {

    companion object {
        private const val TO_TODAY = "to_today"
        private const val TO_TOTAL = "to_total"
        private const val TO_FLAG = "to_flag"
        private const val TO_SEARCH_TODO = "to_search"
    }

    private val _memoUiState = MutableStateFlow<List<MemoDataItem>>(emptyList())
    val memoUiState = _memoUiState.asStateFlow()

    init {
        launchOnIO {
            _memoUiState.emit(memoDataItemSample)
        }
    }


    override val controller = listOf(
        MainComponentController(
            TO_TODAY,
            R.drawable.to_today
        ),
        MainComponentController(
            TO_TOTAL,
            R.drawable.to_total
        ),
        MainComponentController(
            TO_FLAG,
            R.drawable.to_flag
        ),
        MainComponentController(
            TO_SEARCH_TODO,
            R.drawable.to_search
        )
    )
}

data class MemoDataItem(
    val content: String = "",
    val time: Long = System.currentTimeMillis(),
)
