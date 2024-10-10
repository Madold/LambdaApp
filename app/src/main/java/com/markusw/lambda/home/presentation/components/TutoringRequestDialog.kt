@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.lambda.home.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.markusw.lambda.core.presentation.components.OutlinedButton
import com.markusw.lambda.core.presentation.components.SmallButton
import com.markusw.lambda.core.presentation.components.TextField
import com.markusw.lambda.home.presentation.HomeEvent
import com.markusw.lambda.home.presentation.HomeState
import com.markusw.lambda.home.presentation.TopicFilter

@Composable
fun TutoringRequestDialog(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {

    var isComboBoxExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    Dialog(onDismissRequest = {
        if (!state.isSavingMentoring) {
            onEvent(HomeEvent.ChangeRequestTutoringDialogVisibility(false))
        }
    }) {

        Card {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Hacer solicitud de tutoría",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                TextField(
                    value = state.mentoringTitle,
                    onValueChange = { onEvent(HomeEvent.ChangeMentoringTitle(it)) },
                    labelText = "Titulo"
                )
                TextField(
                    value = state.mentoringRequesterDescription,
                    onValueChange = { onEvent(HomeEvent.ChangeMentoringRequesterDescription(it)) },
                    labelText = "Descripción"
                )

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = isComboBoxExpanded,
                    onExpandedChange = {
                        isComboBoxExpanded = !isComboBoxExpanded
                    }
                ) {
                    OutlinedTextField(
                        value = state.mentoringTopic,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isComboBoxExpanded)
                        },
                        modifier = Modifier.menuAnchor(),
                        label = {
                            Text("Tema")
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = isComboBoxExpanded,
                        onDismissRequest = { isComboBoxExpanded = false }
                    ) {
                        TopicFilter.entries.forEach { topic ->
                            DropdownMenuItem(
                                text = { Text(text = topic.label) },
                                onClick = {
                                    isComboBoxExpanded = false
                                    onEvent(HomeEvent.ChangeMentoringTopic(topic.label))
                                }
                            )
                        }
                    }

                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            if (!state.isSavingMentoring) {
                                onEvent(HomeEvent.ChangeRequestTutoringDialogVisibility(false))
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors(),
                        enabled = !state.isSavingMentoring
                    ) {
                        Text(text = "Cancelar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    SmallButton(
                        onClick = { onEvent(HomeEvent.CreateMentoringRequest) },
                        enabled = !state.isSavingMentoring
                    ) {
                        Text(text = if (state.isSavingMentoring) "Solicitando" else "Solicitar")
                        AnimatedVisibility(visible = state.isSavingMentoring) {
                            Spacer(modifier = Modifier.width(8.dp))
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

            }
        }

    }

}