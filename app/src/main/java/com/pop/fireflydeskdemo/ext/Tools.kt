package com.pop.fireflydeskdemo.ext

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


/**
 * px转dp,sp
 */
data class Px(val size: Int)

inline val Int.px: Px get() = Px(this)
inline val Px.dp: Dp get() = (size / 2.25).dp

inline val Px.sp: TextUnit get() = (size / 2.25).sp


fun ViewModel.launchOnIO(
    context: CoroutineContext = Dispatchers.IO,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewModelScope.launch(
        context, start, block
    )
}

fun ViewModel.launchOnMain(
    context: CoroutineContext = Dispatchers.Main,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewModelScope.launch(
        context, start, block
    )
}



fun <T> List<T>.distanceBetween(value1: T, value2: T): Int {
    val index1 = indexOf(value1)
    val index2 = indexOf(value2)

    return if (index1 != -1 && index2 != -1) {
        index2 - index1
    } else {
        0 // 有一个元素没找到
    }
}