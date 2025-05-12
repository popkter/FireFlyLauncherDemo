package com.pop.fireflydeskdemo.launcher.memo

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.pop.fireflydeskdemo.R
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.launchOnIO
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ext.sp
import com.pop.fireflydeskdemo.ui.theme.AppTheme
import com.pop.fireflydeskdemo.ui.theme.LocalFireFlyColors
import com.pop.fireflydeskdemo.ui.theme.LocalTextColors
import com.pop.fireflydeskdemo.ui.theme.Mulish
import com.pop.fireflydeskdemo.vm.base.MainComponentController
import com.pop.fireflydeskdemo.vm.base.MainComponentViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.min


private const val TAG = "MemoComponent"

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MemoComponent(
    modifier: Modifier = Modifier,
    memoUiState: List<MemoDataItem> = memoDataItemSample,
) {

    val list =
        Array(1) { MemoDataItem() }.toList() + memoUiState + Array(3) { MemoDataItem() }.toList()

    val fireFlyColors = LocalFireFlyColors.current
    val textColors = LocalTextColors.current

    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()

    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)


    val layoutInfo by remember { derivedStateOf { listState.layoutInfo } }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
//                Log.e(TAG, "当前可见项: ${visibleItems.map { it.index }}")
            }
    }

    BoxWithConstraints(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(fireFlyColors.light, MaterialTheme.shapes.extraLarge)
    ) {


        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
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
            verticalArrangement = Arrangement.spacedBy(40.px.dp),
            contentPadding = PaddingValues(bottom = 80.px.dp)
        ) {
            itemsIndexed(list) { index, item ->

                val itemInfo = layoutInfo.visibleItemsInfo.find { it.index == index }

//                Log.e(TAG, "MemoComponent index: $index itemInfo offset: ${itemInfo?.offset} itemInfo size: ${itemInfo?.size} startOffset: ${layoutInfo.viewportStartOffset} endOffset: ${layoutInfo.viewportEndOffset}")

                val center = 400

                val itemCenter = itemInfo?.let {
                    it.offset + it.size / 2
                } ?: 0

//                Log.e(TAG, "MemoComponent center: $center itemCenter: $itemCenter ")

                val distanceFromCenter = abs(center - itemCenter).toFloat()

                val norm = min(1f, distanceFromCenter / 200f) // normalize distance
                val scale = 1f - 0.1f * norm
                val alpha = 1f - 0.5f * norm

                if (list[index].content.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .clickable {
                                scope.launch {
                                    listState.animateScrollToItem(index, -280)
                                }
                            }
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                this.alpha = alpha
                            }
                            .fillMaxWidth()
                            .height(240.px.dp)
                            .background(fireFlyColors.blueSky)
                            .padding(start = 200.px.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "$index ${list[index].content}",
                            color = textColors.onGray,
                            fontSize = 60.px.sp,
                            fontFamily = Mulish,
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.px.dp)
//                            .background(fireFlyColors.grape)
                    ) {}
                }
            }
        }

        Canvas(Modifier
            .fillMaxSize()
            .padding(20.px.dp)) {
            drawCircle(
                fireFlyColors.light,
                style = Stroke(40.dp.toPx())
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


@HiltViewModel
class MemoViewModel @Inject constructor() : MainComponentViewModel() {

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
