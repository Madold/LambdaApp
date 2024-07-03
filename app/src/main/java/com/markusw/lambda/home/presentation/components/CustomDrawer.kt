package com.markusw.lambda.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.markusw.lambda.R
import com.markusw.lambda.home.presentation.NavigationItem

@Composable
fun CustomDrawer(
    selectedNavigationItem: NavigationItem,
    onNavigationItemClick: (NavigationItem) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(0.6f)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(8.dp),
            contentAlignment = Alignment.Center

        ) {
            Image(
                painter = painterResource(id = R.drawable.lambda_logo),
                contentDescription = null,
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
        NavigationItem.entries.toTypedArray().take(2).forEach { navigationItem ->
            NavigationItemView(
                navigationItem = navigationItem,
                selected = navigationItem == selectedNavigationItem,
                onClick = { onNavigationItemClick(navigationItem) }
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        Spacer(modifier = Modifier.weight(1f))
        NavigationItem.entries.toTypedArray().takeLast(1).forEach { navigationItem ->
            NavigationItemView(
                navigationItem = navigationItem,
                selected = false,
                onClick = {
                    when (navigationItem) {
                        NavigationItem.Shopping -> {
                            onNavigationItemClick(NavigationItem.Shopping)
                        }

                        else -> {}
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

    }

}