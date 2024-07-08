package com.markusw.lambda.video.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.markusw.lambda.R
import io.getstream.video.android.core.call.state.CustomAction

@Composable
fun ToggleScreenShareAction(
    isScreenSharing: Boolean,
    onCallAction: (ShareScreenAction) -> Unit
) {

    IconButton(
        onClick = {
            onCallAction(
                ShareScreenAction(isEnabled = !isScreenSharing)
            )
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isScreenSharing) Color(0xFFDC433B) else Color(0xFF19232D)
        )
    ) {
        Icon(
            painter = painterResource(id = if (isScreenSharing) R.drawable.ic_screen_share_off else R.drawable.ic_screen_share),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surface
        )
    }

}

data class ShareScreenAction(
    val isEnabled: Boolean
) : CustomAction(tag = "Share screen")