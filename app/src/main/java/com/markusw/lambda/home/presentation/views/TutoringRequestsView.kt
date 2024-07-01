@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.lambda.home.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.markusw.lambda.R
import com.markusw.lambda.core.presentation.components.ExtraSmallButton
import com.markusw.lambda.home.presentation.HomeEvent
import com.markusw.lambda.home.presentation.HomeState
import com.markusw.lambda.home.presentation.components.ProvideMentoringDialog
import com.markusw.lambda.home.presentation.components.TutoringRequestCard
import com.markusw.lambda.home.presentation.components.TutoringRequestDialog

@Composable
fun TutoringRequestView(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {

    val pendingRequests = remember(state.tutorials) {
        state.tutorials.filter { it.author == null }
    }

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
            }
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(pendingRequests, key = { it.roomId }) { mentoring ->
                TutoringRequestCard(
                    mentoring = mentoring,
                    onEvent = onEvent,
                )
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