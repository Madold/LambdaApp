package com.markusw.lambda.home.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.markusw.lambda.home.presentation.views.LiveTutorialsView
import com.markusw.lambda.home.presentation.views.TutoringRequestView

@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {

    val bottomNavController = rememberNavController()
    var selectedDestinationRoute by rememberSaveable {
        mutableStateOf(HomeBottomDestination.LiveTutorials.route)
    }
    
    Scaffold(
        bottomBar = {
            NavigationBar {
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
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(text = destination.label)
                        }
                    )
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
                    onEvent = onEvent
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