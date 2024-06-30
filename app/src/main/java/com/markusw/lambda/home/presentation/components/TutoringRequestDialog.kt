package com.markusw.lambda.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.markusw.lambda.core.presentation.components.SmallButton
import com.markusw.lambda.core.presentation.components.TextField
import com.markusw.lambda.home.presentation.HomeEvent
import com.markusw.lambda.home.presentation.HomeState

@Composable
fun TutoringRequestDialog(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {

//    val imagePickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickVisualMedia(),
//        onResult = { coverUri ->
//            onEvent(HomeEvent.ChangeMentoringCoverUrl(coverUri.toString()))
//        }
//    )

    Dialog(onDismissRequest = {
        onEvent(HomeEvent.ChangeRequestTutoringDialogVisibility(false))
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
                    label = {
                        Text(text = "Titulo")
                    }
                )
                TextField(
                    value = state.mentoringDescription,
                    onValueChange = { onEvent(HomeEvent.ChangeMentoringDescription(it)) },
                    label = {
                        Text(text = "Descripción")
                    }
                )

//                SmallButton(onClick = { imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
//                    Text(text = "Escojer portada")
//                }
//
//                AsyncImage(
//                    model = Uri.parse(state.mentoringCoverUrl),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(10.dp))
//                    ,
//                    contentScale = ContentScale.FillWidth,
//                    error = painterResource(id = R.drawable.pick_image),
//                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SmallButton(
                        onClick = {
                            onEvent(
                                HomeEvent.ChangeRequestTutoringDialogVisibility(
                                    false
                                )
                            )
                        },
                        colors = ButtonDefaults.outlinedButtonColors()
                    ) {
                        Text(text = "Cancelar")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    SmallButton(onClick = { onEvent(HomeEvent.CreateMentoringRequest) }) {
                        Text(text = "Solicitar")
                    }
                }

            }
        }

    }

}