package com.markusw.lambda.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.markusw.lambda.R
import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.presentation.components.SmallButton
import com.markusw.lambda.core.presentation.components.TextField
import com.markusw.lambda.home.presentation.HomeEvent

@Composable
fun DonationDialog(
    mentoring: Mentoring,
    onEvent: (HomeEvent) -> Unit,
    onDismissRequest: () -> Unit
) {

    var amount by rememberSaveable {
        mutableLongStateOf(0L)
    }

    Dialog(onDismissRequest = onDismissRequest) {

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {

                Text(
                    text = "Donar",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Selecciona el método de pago")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = true,
                        onClick = { },
                    )

                    Image(
                        painter = painterResource(id = R.drawable.nequi_logo),
                        contentDescription = null,
                        modifier = Modifier.size(140.dp)
                    )
                }

                TextField(
                    labelText = "Monto",
                    value = amount.toString(),
                    onValueChange = { newAmount ->
                        try {
                            newAmount.toLong().also { amount = it }
                        } catch (e: NumberFormatException) {
                            amount = 0
                        }
                    },
                    prefix = {
                        Text(text = "COP: ")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    SmallButton(
                        onClick = {
                            onEvent(HomeEvent.DonateToMentoring(mentoring, amount))
                            onDismissRequest()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_donation),
                            contentDescription = null
                        )
                        Text(text = "Donar")
                    }
                }

            }
        }

    }
}