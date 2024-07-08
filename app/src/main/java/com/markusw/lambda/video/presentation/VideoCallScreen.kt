package com.markusw.lambda.video.presentation

import android.Manifest
import android.app.Activity
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.markusw.lambda.R
import com.markusw.lambda.core.presentation.components.SmallButton
import com.markusw.lambda.core.utils.ext.pop
import com.markusw.lambda.video.presentation.components.ShareScreenAction
import com.markusw.lambda.video.presentation.components.ToggleScreenShareAction
import io.getstream.video.android.compose.permission.rememberCallPermissionsState
import io.getstream.video.android.compose.ui.components.call.activecall.CallContent
import io.getstream.video.android.compose.ui.components.call.controls.actions.DefaultOnCallActionHandler
import io.getstream.video.android.compose.ui.components.call.controls.actions.FlipCameraAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.ToggleCameraAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.ToggleMicrophoneAction
import io.getstream.video.android.core.call.state.LeaveCall

@Composable
fun VideoCallScreen(
    state: VideoCallState,
    onEvent: (VideoCallEvent) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val mediaProjectionManager = context.getSystemService(MediaProjectionManager::class.java)
    val mediaProjectionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { state.call?.startScreenSharing(it) }
                return@rememberLauncherForActivityResult
            }
        }
    )

    val isMicrophoneEnabled = state.call?.microphone?.isEnabled?.collectAsState()
    val isCameraEnabled = state.call?.camera?.isEnabled?.collectAsState()
    val isScreenSharing = state.call?.screenShare?.isEnabled?.collectAsState()

    Scaffold(modifier = modifier) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (state.callStatus) {
                CallStatus.Joining -> {
                    Text(text = "Joining Call")
                }

                CallStatus.Ended -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.video_call),
                            contentDescription = null,
                        )

                        Text(text = "Llamada finalizada")

                        SmallButton(onClick = { navController.pop() }) {
                            Text(text = "Volver")
                        }
                    }
                }

                else -> {
                    val basePermissions = listOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                    )
                    val bluetoothConnectPermission = if (Build.VERSION.SDK_INT >= 31) {
                        listOf(Manifest.permission.BLUETOOTH_CONNECT)
                    } else {
                        emptyList()
                    }
                    val notificationPermission = if (Build.VERSION.SDK_INT >= 33) {
                        listOf(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        emptyList()
                    }

                    state.call?.let { call ->
                        CallContent(
                            call = call,
                            modifier = Modifier
                                .fillMaxSize(),
                            permissions = rememberCallPermissionsState(
                                call = call,
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
                            controlsContent = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    ToggleCameraAction(
                                        isCameraEnabled = isCameraEnabled?.value ?: false,
                                        onCallAction = { action ->
                                            call.camera.setEnabled(action.isEnabled)
                                        }
                                    )

                                    ToggleMicrophoneAction(
                                        isMicrophoneEnabled = isMicrophoneEnabled?.value ?: false,
                                        onCallAction = { action ->
                                            call.microphone.setEnabled(action.isEnabled)
                                        }
                                    )

                                    FlipCameraAction(
                                        onCallAction = {
                                            call.camera.flip()
                                        }
                                    )

                                    ToggleScreenShareAction(
                                        isScreenSharing = isScreenSharing?.value ?: false,
                                        onCallAction = { action ->
                                            if (isScreenSharing?.value == true) {
                                                call.stopScreenSharing()

                                                return@ToggleScreenShareAction
                                            }

                                            mediaProjectionLauncher.launch(mediaProjectionManager.createScreenCaptureIntent())
                                        }
                                    )

                                }
                            },
                            onCallAction = { action ->
                                if (action == LeaveCall) {
                                    onEvent(VideoCallEvent.Disconnect)
                                }

                                DefaultOnCallActionHandler.onCallAction(call, action)
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


}