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
fun ToggleRecordCallAction(
    isRecording: Boolean,
    onCallAction: (RecordCallAction) -> Unit
) {

    IconButton(
        onClick = {
            onCallAction(
                RecordCallAction(isRecording = !isRecording)
            )
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isRecording) Color(0xFFDC433B) else Color(0xFF19232D)
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_record),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surface
        )
    }

}

data class RecordCallAction(
    val isRecording: Boolean
) : CustomAction(tag = "Share screen")