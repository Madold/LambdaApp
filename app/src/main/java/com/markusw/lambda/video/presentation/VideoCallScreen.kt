package com.markusw.lambda.video.presentation

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import io.getstream.video.android.compose.permission.rememberCallPermissionsState
import io.getstream.video.android.compose.ui.components.call.activecall.CallContent
import io.getstream.video.android.compose.ui.components.call.controls.actions.DefaultOnCallActionHandler
import io.getstream.video.android.core.call.state.LeaveCall

@Composable
fun VideoCallScreen(
    state: VideoCallState,
    onEvent: (VideoCallEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        when {
            state.callStatus == CallStatus.Joining -> {
                Text(text = "Joining Call")
            }
            state.callStatus == CallStatus.Ended -> {
                Text(text = "Call ended")
            }
            else -> {
                val basePermissions = listOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                )
                val bluetoothConnectPermission = if(Build.VERSION.SDK_INT >= 31) {
                    listOf(Manifest.permission.BLUETOOTH_CONNECT)
                } else {
                    emptyList()
                }
                val notificationPermission = if(Build.VERSION.SDK_INT >= 33) {
                    listOf(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    emptyList()
                }
                val context = LocalContext.current

                state.call?.let {
                    CallContent(
                        call = it,
                        modifier = Modifier
                            .fillMaxSize(),
                        permissions = rememberCallPermissionsState(
                            call = state.call,
                            permissions = basePermissions + bluetoothConnectPermission + notificationPermission,
                            onPermissionsResult = { permissions ->
                                if (permissions.values.contains(false)) {
                                    Toast.makeText(
                                        context,
                                        "Please grant all permissions to use this app.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    onEvent(VideoCallEvent.JoinCall)
                                }
                            }
                        ),
                        onCallAction = { action ->
                            if (action == LeaveCall) {
                                onEvent(VideoCallEvent.Disconnect)
                            }

                            DefaultOnCallActionHandler.onCallAction(state.call, action)
                        },
                        onBackPressed = {
                            onEvent(VideoCallEvent.Disconnect)
                        }
                    )
                }
            }

        }
    }

}