package com.markusw.lambda.home.presentation

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.markusw.lambda.core.presentation.components.AdmobBanner
import com.markusw.lambda.home.presentation.components.CustomDrawer
import com.markusw.lambda.home.presentation.views.LiveTutorialsView
import com.markusw.lambda.home.presentation.views.ShoppingView
import com.markusw.lambda.home.presentation.views.TutoringRequestView
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {

    val bottomNavController = rememberNavController()
    val homeNavController = rememberNavController()
    var selectedDestinationRoute by rememberSaveable {
        mutableStateOf(HomeBottomDestination.LiveTutorials.route)
    }
    var drawerState by remember {
        mutableStateOf(CustomDrawerState.Closed)
    }
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density
    var selectedNavigationItem by remember {
        mutableStateOf(NavigationItem.Home)
    }
    val context = LocalContext.current

    val permissionsLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions.values.contains(false)) {
            Toast.makeText(context, "Deber de aceptar todos los permisos para continuar", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permisos concedidos", Toast.LENGTH_SHORT).show()
        }
    }

    val screenWidth = remember {
        derivedStateOf { (configuration.screenWidthDp * density).roundToInt() }
    }

    val offsetValue by remember { derivedStateOf { (screenWidth.value / 4.5).dp } }

    val animatedOffset by animateDpAsState(
        targetValue = if (drawerState.isOpened()) offsetValue else 0.dp,
        label = "Animated Offset"
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpened()) 0.9f else 1f,
        label = "Animated Scale"
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionsLauncher.launch(arrayOf(
                        Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.POST_NOTIFICATIONS
                ))
            }
        } else {
            permissionsLauncher.launch(arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ))
        }
    }

    BackHandler(enabled = drawerState.isOpened()) {
        drawerState = CustomDrawerState.Closed
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
            .fillMaxSize()
    ) {
        CustomDrawer(
            selectedNavigationItem = selectedNavigationItem,
            onNavigationItemClick = {
                selectedNavigationItem = it

                if (it == NavigationItem.Exit) {
                    onEvent(HomeEvent.Logout)
                }

            },
            onClose = {
                drawerState = CustomDrawerState.Closed
            },
            modifier = Modifier
                .statusBarsPadding(),
            user = state.loggedUser
        )

        NavHost(
            navController = homeNavController,
            startDestination = NavigationItem.Home.label
        ) {
            composable(route = NavigationItem.Home.label) {
                Scaffold(
                    modifier = Modifier
                        .offset(x = animatedOffset)
                        .scale(scale = animatedScale),
                    bottomBar = {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AdmobBanner(
                                modifier = Modifier.fillMaxWidth()
                            )
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.surface
                            ) {
                                HomeBottomDestination.entries().forEach { destination ->
                                    NavigationBarItem(
                                        selected = selectedDestinationRoute == destination.route,
                                        onClick = {
                                            selectedDestinationRoute = destination.route
                                            bottomNavController.navigate(destination.route) {
                                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                painter = painterResource(id = destination.icon),
                                                contentDescription = null,
                                            )
                                        },
                                        label = {
                                            Text(text = destination.label)
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedTextColor = MaterialTheme.colorScheme.surface
                                        )
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = bottomNavController,
                        startDestination = HomeBottomDestination.LiveTutorials.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        composable(HomeBottomDestination.LiveTutorials.route) {
                            LiveTutorialsView(
                                state = state,
                                onEvent = onEvent,
                                onNavigationButtonClick = {
                                    drawerState = CustomDrawerState.Opened
                                }
                            )
                        }

                        composable(HomeBottomDestination.TutoringRequests.route) {
                            TutoringRequestView(
                                state = state,
                                onEvent = onEvent
                            )
                        }

                    }
                }
            }

            composable(route = NavigationItem.Shopping.label) {
                ShoppingView(
                    homeNavController = homeNavController
                )
            }
        }
    }


}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScreenPreview(modifier: Modifier = Modifier) {
    HomeScreen(
        state = HomeState(),
        onEvent = {}
    )
}