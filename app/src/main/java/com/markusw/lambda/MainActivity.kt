package com.markusw.lambda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.markusw.lambda.auth.presentation.LoginScreen
import com.markusw.lambda.auth.presentation.LoginViewModel
import com.markusw.lambda.auth.presentation.LoginViewModelEvent
import com.markusw.lambda.core.presentation.Screens
import com.markusw.lambda.core.utils.ext.pop
import com.markusw.lambda.home.presentation.HomeScreen
import com.markusw.lambda.home.presentation.HomeViewModel
import com.markusw.lambda.home.presentation.HomeViewModelEvent
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

        val isUserLoggedIn = Firebase.auth.currentUser != null

        setContent {
            val navController = rememberNavController()

            LambdaAppTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                NavHost(
                    navController = navController,
                    startDestination = if (isUserLoggedIn) Screens.Home.route else Screens.Login.route
                ) {
                    composable(route = Screens.Login.route) {
                        val viewModel = hiltViewModel<LoginViewModel>()

                        LaunchedEffect(key1 = Unit) {
                            viewModel.events.collectLatest { viewModelEvent ->
                                when (viewModelEvent) {
                                    is LoginViewModelEvent.AuthFailed -> {

                                    }
                                    LoginViewModelEvent.AuthSuccessful -> {
                                        navController.navigate(Screens.Home.route) {
                                            popUpTo(Screens.Login.route) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        LoginScreen(
                            onEvent = viewModel::onEvent
                        )
                    }

                    composable(route = Screens.Home.route) {
                        val viewModel = hiltViewModel<HomeViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        LaunchedEffect(key1 = Unit) {
                            viewModel.events.collectLatest { event ->
                                when (event) {
                                    is HomeViewModelEvent.VideoClientInitialized -> {
                                        navController.navigate("${Screens.Video.route}/${event.roomId}")
                                    }
                                }
                            }
                        }

                        HomeScreen(
                            state = state,
                            onEvent = viewModel::onEvent
                        )
                    }

                    composable(
                        route = "${Screens.Video.route}/{roomId}",
                        arguments = listOf(
                            navArgument("roomId") {
                                type = NavType.StringType
                            }
                        )
                    ) {
                        val viewModel = hiltViewModel<VideoCallViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        LaunchedEffect(key1 = Unit) {
                            viewModel.events.collectLatest { viewModelEvent ->
                                when (viewModelEvent) {
                                    VideoCallViewModelEvent.CallLeaved -> {
                                        navController.pop()
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
