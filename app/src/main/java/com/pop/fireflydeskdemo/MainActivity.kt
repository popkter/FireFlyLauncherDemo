package com.pop.fireflydeskdemo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.ui.compose.BottomBar
import com.pop.fireflydeskdemo.ui.compose.MainComponent
import com.pop.fireflydeskdemo.ui.compose.SecondComponent
import com.pop.fireflydeskdemo.ui.compose.TopBar
import com.pop.fireflydeskdemo.ui.theme.FireFlyDeskDemoTheme
import com.pop.fireflydeskdemo.ui.theme.Lime
import com.pop.fireflydeskdemo.ui.theme.Night
import com.pop.fireflydeskdemo.ui.theme.componentRadius
import com.pop.fireflydeskdemo.vm.DateTimeUiState
import com.pop.fireflydeskdemo.vm.DateTimeUiStateSample
import com.pop.fireflydeskdemo.vm.DateViewModel

class MainActivity : ComponentActivity() {

    private val dateViewModel by viewModels<DateViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        hideSystemUI()
        setContent {
            FireFlyDeskDemoTheme {

                val dateTimeUiState by dateViewModel.dateTimeUiState.collectAsState()

                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Night))

                BasicContainer(modifier = Modifier.fillMaxSize(), dateTimeUiState)
            }
        }
    }


    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController.apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BasicContainer(modifier: Modifier = Modifier, dateTime: DateTimeUiState = DateTimeUiStateSample) {


    BoxWithConstraints(
        modifier = modifier
            .background(
                color = Lime, shape = RoundedCornerShape(componentRadius)
            )
            .clip(RoundedCornerShape(componentRadius)),
    ) {

        // maxWidth 和 maxHeight 是父容器的约束
        Box(
            modifier = Modifier
                .then(Modifier.layout { measurable, _ ->
                    // 不传入 parentConstraints，表示忽略父限制
                    val placeable = measurable.measure(Constraints())
                    layout(placeable.width, placeable.height) {
                        placeable.place(0, 0)
                    }
                })
                .size(2880.px.dp, 2060.px.dp)
        ) {

            MainComponent(
                Modifier.fillMaxSize(),
                dateTime
            )
        }

        TopBar(
            Modifier
                .padding(top = 50.px.dp, start = 50.px.dp)
                .height(400.px.dp)
                .widthIn(710.px.dp, 1910.px.dp)
                .align(Alignment.TopStart),
            dateTime
        )



        SecondComponent(
            Modifier
                .padding(start = 50.px.dp, bottom = 300.px.dp)
                .align(Alignment.BottomStart)
        )

        BottomBar(
            Modifier
                .padding(start = 50.px.dp, bottom = 50.px.dp)
                .align(Alignment.BottomStart)
        )


    }
}


@Preview(
    showBackground = true, showSystemUi = false
)
@Composable
fun GreetingPreview() {
    FireFlyDeskDemoTheme {
        BasicContainer()
    }
}