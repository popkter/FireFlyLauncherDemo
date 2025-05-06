package com.pop.fireflydeskdemo.ui.compose

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pop.fireflydeskdemo.ext.dp
import com.pop.fireflydeskdemo.ext.px
import com.pop.fireflydeskdemo.vm.DockViewModel

@Composable
fun BottomBar(modifier: Modifier = Modifier, dockViewModel: DockViewModel) {

    val dockIconsUiState by dockViewModel.dockIconsUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    Box(modifier) {
        LazyRow(
            modifier = Modifier
                .wrapContentWidth()
                .height(200.px.dp)
                .background(color = MaterialTheme.colorScheme.scrim, shape = MaterialTheme.shapes.large)
                .padding(horizontal = 50.px.dp)
                .align(Alignment.TopEnd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(80.px.dp),
        ) {

            items(dockIconsUiState){
                Icon(
                    painter = painterResource(it.dockInfo.iconRes),
                    contentDescription = it.dockInfo.desc,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(120.px.dp)
                        .clickable {
                            Toast.makeText(
                                context,
                                it.dockInfo.desc,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                )
            }
        }
    }
}