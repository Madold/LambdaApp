package com.markusw.lambda.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.markusw.lambda.R
import com.markusw.lambda.home.presentation.DonationState
import com.markusw.lambda.home.presentation.HomeState

@Composable
fun DonationStatusDialog(
    state: HomeState
) {

    Dialog(onDismissRequest = {  }) {
        Card {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (state.donationState) {
                    DonationState.DonationSuccess -> {
                        Image(
                            painter = painterResource(id = R.drawable.check_mark),
                            contentDescription = null
                        )
                        Text(text = "Donación exitosa :)")
                    }
                    
                    DonationState.InProgress -> {
                        CircularProgressIndicator()    
                        Text(text = "Realizando donación")
                    }
                }
            }
        }

    }

}