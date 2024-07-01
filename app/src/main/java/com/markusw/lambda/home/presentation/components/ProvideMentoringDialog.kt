package com.markusw.lambda.home.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.markusw.lambda.R
import com.markusw.lambda.core.presentation.components.SmallButton
import com.markusw.lambda.core.presentation.components.TextField
import com.markusw.lambda.home.presentation.HomeEvent
import com.markusw.lambda.home.presentation.HomeState

@Composable
fun ProvideMentoringDialog(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { coverUri ->
            onEvent(HomeEvent.ChangeMentoringCoverUrl(coverUri.toString()))
        }
    )

    Dialog(onDismissRequest = {
        onEvent(HomeEvent.ChangeProvideMentoringDialogVisibility(false))
    }) {
        Card(
            modifier = modifier,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = state.mentoringDescription,
                    onValueChange = {
                        onEvent(HomeEvent.ChangeMentoringDescription(it))
                    },
                    label = {
                        Text(text = "Descripción corta de la tutoría")
                    }
                )
                TextField(
                    value = state.mentoringPrice.toString(),
                    onValueChange = {
                        try {
                            val price = it.toLong()

                            if (price >= 0) {
                                onEvent(HomeEvent.ChangeMentoringPrice(price))
                            }
                        } catch (e: NumberFormatException) {
                            onEvent(HomeEvent.ChangeMentoringPrice(0))
                        }
                    },
                    label = {
                        Text(text = "Precio")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    prefix = {
                        Text(text = "COP")
                    },
                )

                SmallButton(onClick = {
                    imagePickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }) {
                    Text(text = if (state.mentoringCoverUri.isBlank()) "Escojer portada" else "Cambiar portada")
                }

                AnimatedVisibility(visible = state.coverUriError != null) {
                    Text(
                        text = state.coverUriError ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                AsyncImage(
                    model = Uri.parse(state.mentoringCoverUri),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.FillWidth,
                    error = painterResource(id = R.drawable.pick_image),
                )

                SmallButton(onClick = {
                    onEvent(HomeEvent.StartLiveMentoring)
                }, enabled = !state.isStartingLiveMentoring) {

                    Text(text = if (state.isStartingLiveMentoring) "Iniciando llamada" else "Iniciar tutoría")

                    Spacer(modifier = Modifier.width(8.dp))

                    AnimatedVisibility(visible = state.isStartingLiveMentoring) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.surface
                        )
                    }

                }

            }
        }
    }

}