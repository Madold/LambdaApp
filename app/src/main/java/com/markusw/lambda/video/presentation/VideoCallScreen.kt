package com.markusw.lambda.video.presentation

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.markusw.lambda.R
import com.markusw.lambda.core.presentation.components.SmallButton
import com.markusw.lambda.core.utils.ext.pop
import io.getstream.video.android.compose.permission.rememberCallPermissionsState
import io.getstream.video.android.compose.ui.components.call.activecall.CallContent
import io.getstream.video.android.compose.ui.components.call.controls.actions.DefaultOnCallActionHandler
import io.getstream.video.android.core.call.state.LeaveCall

@Composable
fun VideoCallScreen(
    state: VideoCallState,
    onEvent: (VideoCallEvent) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    Scaffold(modifier = modifier) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
            ,
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



}