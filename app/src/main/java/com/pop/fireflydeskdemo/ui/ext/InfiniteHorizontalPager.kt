package com.pop.fireflydeskdemo.ui.ext

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.TargetedFlingBehavior
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pop.fireflydeskdemo.launcher.floorMod


/**
 * 一个支持无限循环滚动的 HorizontalPager 封装组件。
 *
 * 实现原理：
 * - 将实际页数 actualPageCount 映射到一个非常大的虚拟页数 Int.MAX_VALUE。
 * - 初始页设置在中间 (Int.MAX_VALUE / 2)，从而支持前后无限滑动。
 * - 每一页都会通过模运算映射回真实页索引（0 到 actualPageCount - 1）。
 *
 * 用法与原生 HorizontalPager 几乎一致，只需额外提供 actualPageCount。
 *
 * @param state PagerState，通常通过 rememberPagerState(initialPage = Int.MAX_VALUE / 2) 创建
 * @param actualPageCount 实际的逻辑页面数量（非无限页数）
 * @param modifier 用于修改 Pager 的布局行为
 * @param contentPadding 页内容的内边距
 * @param pageSize 控制每一页的大小（默认 Fill）
 * @param beyondViewportPageCount 视口外预加载页数
 * @param pageSpacing 页之间的间距
 * @param verticalAlignment 垂直对齐方式
 * @param flingBehavior 控制 fling 动画行为
 * @param userScrollEnabled 是否允许用户滑动
 * @param reverseLayout 是否反转滚动方向
 * @param key 为每一页生成唯一键，避免重组问题
 * @param pageNestedScrollConnection 嵌套滚动连接
 * @param snapPosition 对齐位置
 * @param pageContent 每一页的内容，提供实际页索引和滑动偏移
 */
@Composable
fun InfiniteHorizontalPager(
    state: PagerState,
    actualPageCount: Int,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    pageSize: PageSize = PageSize.Fill,
    beyondViewportPageCount: Int = PagerDefaults.BeyondViewportPageCount,
    pageSpacing: Dp = 0.dp,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    flingBehavior: TargetedFlingBehavior = PagerDefaults.flingBehavior(state = state),
    userScrollEnabled: Boolean = true,
    reverseLayout: Boolean = false,
    key: ((index: Int) -> Any)? = null,
    pageNestedScrollConnection: NestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
        state,
        Orientation.Horizontal
    ),
    snapPosition: SnapPosition = SnapPosition.Start,
    pageContent: @Composable PagerScope.(actualPageIndex: Int, virtualPageIndex: Int) -> Unit
) {
    HorizontalPager(
        state = state,
        modifier = modifier,
        contentPadding = contentPadding,
        pageSize = pageSize,
        beyondViewportPageCount = beyondViewportPageCount,
        pageSpacing = pageSpacing,
        verticalAlignment = verticalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        reverseLayout = reverseLayout,
        key = key,
        pageNestedScrollConnection = pageNestedScrollConnection,
        snapPosition = snapPosition,
    ) { index ->
        val initialIndex = Int.MAX_VALUE / 2
        val actualIndex = (index - initialIndex).floorMod(actualPageCount)

        pageContent(actualIndex, index)
    }
}


@Composable
fun rememberInfinitePagerState(
    initialPage: Int = Int.MAX_VALUE / 2,
): PagerState {
    return rememberPagerState(
        initialPage = initialPage,
        pageCount = { Int.MAX_VALUE }
    )
}