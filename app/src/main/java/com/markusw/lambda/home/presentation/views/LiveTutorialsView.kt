@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.lambda.home.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markusw.lambda.home.presentation.HomeEvent
import com.markusw.lambda.home.presentation.HomeState
import com.markusw.lambda.home.presentation.components.DonationStatusDialog
import com.markusw.lambda.home.presentation.components.JoiningLiveSessionDialog
import com.markusw.lambda.home.presentation.components.LiveMentoringCard
import com.markusw.lambda.home.presentation.components.PaymentStatusDialog

@Composable
fun LiveTutorialsView(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {

    val liveTutorials = remember(state.tutorials) {
        state.tutorials.filter { it.author != null }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp),
            title = { },
            navigationIcon = {
                IconButton(
                    onClick = {

                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(liveTutorials, key = { it.roomId }) { mentoring ->
                LiveMentoringCard(
                    mentoring = mentoring,
                    onEvent = onEvent,
                    modifier = Modifier,
                    loggedUser = state.loggedUser
                )
            }
        }

        if (state.isDonating) {
            DonationStatusDialog(state)
        }

        if (state.isPaymentProcessing) {
            PaymentStatusDialog(state)
        }

        if (state.isJoiningLiveMentoring) {
            JoiningLiveSessionDialog()
        }
    }


}