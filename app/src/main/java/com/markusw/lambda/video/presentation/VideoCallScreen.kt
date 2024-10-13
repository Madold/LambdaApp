package com.markusw.lambda.video.presentation

import android.Manifest
import android.app.Activity
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.markusw.lambda.R
import com.markusw.lambda.core.presentation.components.SmallButton
import com.markusw.lambda.core.utils.ext.pop
import com.markusw.lambda.video.presentation.components.ToggleScreenShareAction
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory
import io.getstream.video.android.compose.permission.rememberCallPermissionsState
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.base.GenericContainer
import io.getstream.video.android.compose.ui.components.call.activecall.CallContent
import io.getstream.video.android.compose.ui.components.call.controls.actions.DefaultOnCallActionHandler
import io.getstream.video.android.compose.ui.components.call.controls.actions.FlipCameraAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.LeaveCallAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.ToggleCameraAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.ToggleMicrophoneAction
import io.getstream.video.android.core.Call
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
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

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

                CallStatus.Leaved -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.video_call),
                            contentDescription = null,
                        )

                        Text(text = "Has abandonado la llamada")

                        SmallButton(onClick = { navController.pop() }) {
                            Text(text = "Volver")
                        }
                    }
                }

                CallStatus.WaitingForApproval -> {
                    when (state.chatConnectionStatus) {
                        ChatConnectionStatus.Connected -> {
                            ChatTheme {
                                MessagesScreen(
                                    viewModelFactory = MessagesViewModelFactory(
                                        channelId = "messaging:1234",
                                        context = LocalContext.current,
                                    )
                                )
                            }
                        }

                        ChatConnectionStatus.Connecting -> {
                            Text(text = "Connecting chat...")
                        }

                        ChatConnectionStatus.Error -> {
                            Text(text = "Chat connection erro :(")
                        }
                    }
                }

                CallStatus.Finished -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.video_call),
                            contentDescription = null,
                        )

                        Text(text = "Esta llamada ha sido finalizada")

                        SmallButton(onClick = { navController.pop() }) {
                            Text(text = "Volver")
                        }
                    }
                }

                CallStatus.AccessGranted -> {

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
                            appBarContent = {
                                Row(
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .height(VideoTheme.dimens.componentHeightL),
                                    verticalAlignment = CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    CallAppBarLeftContent(
                                        call = it,
                                        title = ""
                                    )

                                    Column {
                                        LeaveCallAction {
                                            isContextMenuVisible = true
                                        }

                                        DropdownMenu(
                                            expanded = isContextMenuVisible,
                                            onDismissRequest = { isContextMenuVisible = false },
                                            modifier = Modifier.background(
                                                color = Color.White
                                            )
                                        ) {
                                            DropdownMenuItem(
                                                text = {
                                                    Text("Salir")
                                                },
                                                onClick = {
                                                    onEvent(VideoCallEvent.Disconnect)
                                                }
                                            )
                                            if (state.loggedUser.id == state.authorId) {
                                                DropdownMenuItem(
                                                    text = {
                                                        Text("Finalizar")
                                                    },
                                                    onClick = {
                                                        onEvent(VideoCallEvent.FinishSession)
                                                    }
                                                )
                                            }
                                        }

                                    }
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


@Composable
fun RowScope.CallAppBarLeftContent(call: Call, title: String) {
    val isReconnecting by call.state.isReconnecting.collectAsStateWithLifecycle()
    val isRecording by call.state.recording.collectAsStateWithLifecycle()
    val duration by call.state.duration.collectAsStateWithLifecycle()

    CalLLeftContent(
        modifier = Modifier.align(CenterVertically),
        text = duration?.toString() ?: title,
        isRecording = isRecording,
        isReconnecting = isReconnecting,
    )
}

@Composable
private fun CalLLeftContent(
    modifier: Modifier = Modifier,
    text: String,
    isRecording: Boolean,
    isReconnecting: Boolean,
) {
    GenericContainer(modifier = modifier) {
        Row {
            if (isRecording) {
                Box(
                    modifier = Modifier
                        .size(VideoTheme.dimens.componentHeightS)
                        .clip(VideoTheme.shapes.circle)
                        .background(
                            color = VideoTheme.colors.alertWarning,
                            shape = VideoTheme.shapes.circle,
                        )
                        .border(2.dp, VideoTheme.colors.basePrimary, VideoTheme.shapes.circle),
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_encrypted),
                    tint = VideoTheme.colors.alertSuccess,
                    contentDescription = "call encrypted",
                )
            }
            Text(
                modifier = Modifier
                    .padding(
                        start = VideoTheme.dimens.componentPaddingStart,
                        end = VideoTheme.dimens.componentPaddingEnd,
                    ),
                text = if (isReconnecting) {
                    "Reconectando..."
                } else if (isRecording) {
                    "Esta sesión se está grabando"
                } else {
                    text
                },
                fontSize = VideoTheme.dimens.textSizeS,
                color = VideoTheme.colors.baseSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
            )
        }
    }
}