package com.markusw.lambda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.markusw.lambda.auth.presentation.AuthScreen
import com.markusw.lambda.auth.presentation.AuthViewModel
import com.markusw.lambda.auth.presentation.AuthViewModelEvent
import com.markusw.lambda.core.presentation.Screens
import com.markusw.lambda.ui.theme.LambdaAppTheme
import com.markusw.lambda.video.presentation.VideoCallScreen
import com.markusw.lambda.video.presentation.VideoCallViewModel
import com.markusw.lambda.video.presentation.VideoCallViewModelEvent
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.video.android.compose.theme.VideoTheme
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            LambdaAppTheme(
                darkTheme = false
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screens.Auth.route
                ) {
                    composable(route = Screens.Auth.route) {
                        val viewModel = hiltViewModel<AuthViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        LaunchedEffect(key1 = Unit) {
                            viewModel.events.collectLatest { viewModelEvent ->
                                when (viewModelEvent) {
                                    is AuthViewModelEvent.VideoClientInitialized -> {
                                        navController.navigate(Screens.Video.route) {
                                            popUpTo(Screens.Auth.route) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        AuthScreen(
                            state = state,
                            onEvent = viewModel::onEvent
                        )
                    }

                    composable(route = Screens.Video.route) {
                        val viewModel = hiltViewModel<VideoCallViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        LaunchedEffect(key1 = Unit) {
                            viewModel.events.collectLatest { viewModelEvent ->
                                when (viewModelEvent) {
                                    VideoCallViewModelEvent.CallLeaved -> {
                                        navController.navigate(Screens.Auth.route) {
                                            popUpTo(Screens.Video.route) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        VideoTheme {
                            VideoCallScreen(
                                state = state,
                                onEvent = viewModel::onEvent
                            )
                        }
                    }
                }

            }
        }
    }
}
