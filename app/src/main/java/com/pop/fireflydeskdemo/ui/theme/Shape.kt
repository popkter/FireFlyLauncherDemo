package com.pop.fireflydeskdemo.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px

val shapes = Shapes(
    extraSmall = RoundedCornerShape(10.px.dp),
    small = RoundedCornerShape(25.px.dp),
    medium = RoundedCornerShape(50.px.dp),
    large = RoundedCornerShape(100.px.dp),
    extraLarge = RoundedCornerShape(50)
)