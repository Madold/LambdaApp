@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.lambda.home.presentation.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.markusw.lambda.R
import com.markusw.lambda.core.presentation.components.ExtraSmallButton
import com.markusw.lambda.home.presentation.HomeEvent
import com.markusw.lambda.home.presentation.HomeState
import com.markusw.lambda.home.presentation.components.TutoringRequestDialog

@Composable
fun TutoringRequestView(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {
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
    }

    if (state.isRequestTutoringDialogVisible) {
        TutoringRequestDialog(
            state = state,
            onEvent = onEvent
        )
    }

}