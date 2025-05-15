package com.pop.fireflydeskdemo.ui.ext


import android.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pop.fireflydeskdemo.ui.theme.AppTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import kotlin.math.max

@Composable
fun DynamicBorderTextView(
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text(
            text = text,
            fontSize = 100.sp,
            modifier = Modifier
                .align(Alignment.Center)
                .marqueeBorder()
                .background(Color.Gray.copy(alpha = .3F), RoundedCornerShape(53.dp, 0.dp, 53.dp, 0.dp))
                .padding(20.dp)
        )
    }
}

@Composable
fun Modifier.animatedGradientBorderX(
    borderWidth: Dp = 4.dp,
    shape: Shape = RoundedCornerShape(12.dp),
    colors: List<Color> = listOf(
        Color(0xFFFF595A),
        Color(0xFFFFC766),
        Color(0xFF35A07F),
        Color(0xFF35A07F),
        Color(0xFFFFC766),
        Color(0xFFFF595A)
    ),
    animationDuration: Int = 10000
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    this
        .clip(shape)
        .drawWithCache {
            val strokeWidthPx = borderWidth.toPx()
            val gradientBrush = Brush.sweepGradient(colors)
            onDrawWithContent {
                // 绘制旋转的渐变边框
                rotate(rotation) {
                    drawCircle(
                        brush = gradientBrush,
                        radius = size.maxDimension,
                        blendMode = BlendMode.SrcIn
                    )
                }
                // 绘制内容
                drawContent()
            }
        }
}


@Composable
fun Modifier.animatedGradientBorder(
    borderWidth: Dp = 4.dp,
    shape: Shape = RoundedCornerShape(10.dp, 0.dp, 50.dp, 0.dp),
    colors: List<Color> = listOf(
        Color(0xFFFF595A),
        Color(0xFFFFC766),
        Color(0xFF35A07F),
        Color(0xFF35A07F),
        Color(0xFFFFC766),
        Color(0xFFFF595A)
    ),
    animationDuration: Int = 2000
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition()
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(animationDuration, easing = LinearEasing))
    )

    val shaderBrush = remember(colors, rotation) {
        object : ShaderBrush() {
            override fun createShader(size: Size): android.graphics.Shader {
                val centerX = size.width / 2f
                val centerY = size.height / 2f

                val androidColors = colors.map { it.toArgb() }.toIntArray()

                val sweepGradient =
                    android.graphics.SweepGradient(centerX, centerY, androidColors, null)

                // 旋转矩阵，实现颜色流动
                val matrix = Matrix()
                matrix.setRotate(rotation, centerX, centerY)
                sweepGradient.setLocalMatrix(matrix)

                return sweepGradient
            }
        }
    }

    // 动态计算 stops，使颜色位置整体向前平移，实现跑马灯循环
    val stops = remember(animatedOffset, colors) {
        val n = colors.size
        List(n) { index ->
            ((index.toFloat() / (n - 1)) + animatedOffset) % 1f
        }
    }

    val colorStops = remember(colors, stops) {
        colors.zip(stops).map { (color, stop) -> stop to color }.toTypedArray()
    }

    this.then(
        Modifier.drawWithCache {
            val strokeWidthPx = borderWidth.toPx()
            val inset = strokeWidthPx / 2
            val outline = shape.createOutline(size, layoutDirection, this)
            val rect = Rect(0f, 0f, size.width, size.height)

            // 动态线性渐变 Brush
            /*
                        val brush = Brush.linearGradient(
                            colors = colors,
                            start = Offset(size.width * animatedOffset, 0f),
                            end = Offset(0f, size.height * (1 - animatedOffset))
                        )
            */

            // 创建 SweepGradient，中心点是圆心
            /*

                        val brush = Brush.sweepGradient(
                            colorStops = colorStops,
                            center = Offset(size.width / 2f, size.height / 2f)
                        )
            */


//            val brush = Brush.horizontalGradient(
//                colors = colors,
//                startX = size.width * animatedOffset,
//                endX = size.width * (animatedOffset - 1f)  // 往左流动一轮
//            )


            /*            val center = Offset(
                            x = size.width / 2f + size.width * (animatedOffset - 0.5f),
                            y = size.height / 2f + size.height * (animatedOffset - 0.5f)
                        )

                        val radius = max(size.width, size.height) * 0.8f

                        val brush = Brush.radialGradient(
                            colors = colors,
                            center = center,
                            radius = radius
                        )*/

            onDrawWithContent {
                drawContent()

                when (outline) {
                    is Outline.Rounded -> {
                        val path = Path().apply {
                            addRoundRect(
                                RoundRect(
                                    left = inset,
                                    top = inset,
                                    right = size.width - inset,
                                    bottom = size.height - inset,
                                    topLeftCornerRadius = outline.roundRect.topLeftCornerRadius,
                                    topRightCornerRadius = outline.roundRect.topRightCornerRadius,
                                    bottomRightCornerRadius = outline.roundRect.bottomRightCornerRadius,
                                    bottomLeftCornerRadius = outline.roundRect.bottomLeftCornerRadius
                                )
                            )
                        }

                        drawPath(path, shaderBrush, style = Stroke(strokeWidthPx))
                    }

                    is Outline.Rectangle -> {
                        drawRect(
                            brush = shaderBrush,
                            topLeft = Offset(inset, inset),
                            size = Size(size.width - strokeWidthPx, size.height - strokeWidthPx),
                            style = Stroke(strokeWidthPx)
                        )
                    }

                    else -> {
                        // 其他情况按需处理
                    }
                }
            }


//           /* onDrawWithContent {
//                drawContent()
//
//                // 仅绘制边框，使用 shape 的轮廓
//                when (outline) {
//                    is Outline.Rounded -> {
//                        val path = Path().apply {
//                            addRoundRect(
//                                RoundRect(
//                                    left = inset,
//                                    top = inset,
//                                    right = size.width - inset,
//                                    bottom = size.height - inset,
//                                    topLeftCornerRadius = outline.roundRect.topLeftCornerRadius,
//                                    topRightCornerRadius = outline.roundRect.topRightCornerRadius,
//                                    bottomRightCornerRadius = outline.roundRect.bottomRightCornerRadius,
//                                    bottomLeftCornerRadius = outline.roundRect.bottomLeftCornerRadius
//                                )
//                            )
//                        }
//
//                        drawPath(
//                            path = path,
//                            brush = brush,
//                            style = Stroke(width = strokeWidthPx)
//                        )
//                    }
//
//                    is Outline.Rectangle -> {
//                        drawRect(
//                            brush = brush,
//                            topLeft = Offset(inset, inset),
//                            size = Size(size.width - strokeWidthPx, size.height - strokeWidthPx),
//                            style = Stroke(width = strokeWidthPx)
//                        )
//                    }
//
//                    else -> {
//                        // 可扩展处理其他形状
//                    }
//                }
//            }*/
        }
    )
}


@Composable
fun Modifier.marqueeBorder(
    borderWidth: Dp = 10.dp,
    shape: Shape = RoundedCornerShape(50.dp, 0.dp, 50.dp, 0.dp),
    colors: List<Color> = listOf(
        Color(0xFFFF595A),
        Color(0xFFFFC766),
        Color(0xFF35A07F),
        Color(0xFF35A07F),
        Color(0xFFFFC766),
        Color(0xFFFF595A)
    ),
    animationDuration: Int = 2000
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition()

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(animationDuration, easing = LinearEasing))
    )

    val shaderBrush = remember(colors, rotation) {
        object : ShaderBrush() {
            override fun createShader(size: Size): android.graphics.Shader {
                val centerX = size.width / 2f
                val centerY = size.height / 2f

                val androidColors = colors.map { it.toArgb() }.toIntArray()

                val sweepGradient =
                    android.graphics.SweepGradient(centerX, centerY, androidColors, null)

                // 旋转矩阵，实现颜色流动
                val matrix = Matrix()
                matrix.setRotate(rotation, centerX, centerY)
                sweepGradient.setLocalMatrix(matrix)

                return sweepGradient
            }
        }
    }

    this.then(
        Modifier.drawWithCache {
            val strokeWidthPx = borderWidth.toPx()
            val inset = strokeWidthPx / 2
            val outline = shape.createOutline(size, layoutDirection, this)


            onDrawWithContent {
                drawContent()

                when (outline) {
                    is Outline.Rounded -> {
                        val path = Path().apply {
                            addRoundRect(
                                RoundRect(
                                    left = inset,
                                    top = inset,
                                    right = size.width - inset,
                                    bottom = size.height - inset,
                                    topLeftCornerRadius = outline.roundRect.topLeftCornerRadius,
                                    topRightCornerRadius = outline.roundRect.topRightCornerRadius,
                                    bottomRightCornerRadius = outline.roundRect.bottomRightCornerRadius,
                                    bottomLeftCornerRadius = outline.roundRect.bottomLeftCornerRadius
                                )
                            )
                        }

                        drawPath(path, shaderBrush, style = Stroke(strokeWidthPx))
                    }

                    is Outline.Rectangle -> {
                        drawRect(
                            brush = shaderBrush,
                            topLeft = Offset(inset, inset),
                            size = Size(size.width - strokeWidthPx, size.height - strokeWidthPx),
                            style = Stroke(strokeWidthPx)
                        )
                    }

                    else -> {

                    }
                }
            }

        }
    )
}

@Composable
@Preview
fun DynamicBorderTextViewPreview() {

    AppTheme {
        DynamicBorderTextView(
            "Hello"
        )
    }

}