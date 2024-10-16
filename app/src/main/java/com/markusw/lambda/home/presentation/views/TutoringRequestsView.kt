@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.lambda.home.presentation.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.markusw.lambda.R
import com.markusw.lambda.core.presentation.components.ExtraSmallButton
import com.markusw.lambda.home.presentation.HomeEvent
import com.markusw.lambda.home.presentation.HomeState
import com.markusw.lambda.home.presentation.TopicFilter
import com.markusw.lambda.home.presentation.components.ProvideMentoringDialog
import com.markusw.lambda.home.presentation.components.TutoringRequestCard
import com.markusw.lambda.home.presentation.components.TutoringRequestDialog

@Composable
fun TutoringRequestView(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {

    val pendingRequests = remember(state.tutorials, state.selectedTopicFilter) {
        state.tutorials.filter { it.author == null && it.topic == state.selectedTopicFilter.label }
    }
    var isFiltersMenuVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp),
            title = { },
            actions = {
                ExtraSmallButton(onClick = {
                    onEvent(HomeEvent.ChangeRequestTutoringDialogVisibility(true))
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_graduation_cap),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Solicitar tutoría")
                }

                IconButton(onClick = {
                    isFiltersMenuVisible = true
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_filter),
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = isFiltersMenuVisible,
                    onDismissRequest = {
                        isFiltersMenuVisible = false
                    }
                ) {
                    TopicFilter.entries.forEach { topic ->
                        DropdownMenuItem(
                            onClick = {
                                isFiltersMenuVisible = false
                                onEvent(HomeEvent.ChangeSelectedTopicFilter(topic))
                            },
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(topic.label)
                                    if (state.selectedTopicFilter == topic) {
                                        Icon(painter = painterResource(R.drawable.ic_check), contentDescription = null)
                                    }
                                }
                            },
                        )
                    }
                }

            }
        )

        AnimatedContent(
            targetState = pendingRequests.isEmpty(),
            label = "",
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { isEmpty ->
            if (isEmpty) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mentoring_requests),
                        contentDescription = null
                    )
                    Text(text = "Aun no hay solicitudes de tutorías")
                    Text(text = "¡Realiza la primera!")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(pendingRequests, key = { it.roomId }) { mentoring ->

                        val animatable = remember {
                            Animatable(0.5f)
                        }

                        LaunchedEffect(key1 = true) {
                            animatable.animateTo(1f, tween(350, easing = FastOutSlowInEasing))
                        }

                        TutoringRequestCard(
                            mentoring = mentoring,
                            onEvent = onEvent,
                            modifier = Modifier.graphicsLayer {
                                this.scaleY = animatable.value
                                this.scaleX = animatable.value
                            },
                            loggedUser = state.loggedUser
                        )
                    }
                }
            }

        }
    }

    if (state.isRequestTutoringDialogVisible) {
        TutoringRequestDialog(
            state = state,
            onEvent = onEvent
        )
    }

    if (state.isProvideMentoringDialogVisible) {
        ProvideMentoringDialog(
            state = state,
            onEvent = onEvent
        )
    }

}