package com.markusw.lambda.home.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.markusw.lambda.R
import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.presentation.components.SmallButton
import com.markusw.lambda.core.presentation.components.TextField
import com.markusw.lambda.home.presentation.HomeEvent
import com.markusw.lambda.home.presentation.HomeState

@Composable
fun ProvideMentoringDialog(
    mentoring: Mentoring,
    onEvent: (HomeEvent) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {

    var mentoringToStart by remember {
        mutableStateOf(mentoring)
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { coverUri ->
            mentoringToStart = mentoringToStart.copy(
                coverUrl = coverUri.toString()
            )
        }
    )

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = modifier,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = mentoringToStart.description,
                    onValueChange = {
                        mentoringToStart = mentoringToStart.copy(
                            description = it
                        )
                    },
                    label = {
                        Text(text = "Descripción corta de la tutoría")
                    }
                )
                TextField(
                    value = mentoringToStart.price.toString(),
                    onValueChange = {
                        try {
                            val price = it.toLong()

                            if (price >= 0) {
                                mentoringToStart = mentoringToStart.copy(
                                    price = price
                                )
                            }
                        } catch (e: NumberFormatException) {
                            mentoringToStart = mentoringToStart.copy(
                                price = 0
                            )
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
                    Text(text = if (mentoringToStart.coverUrl.isBlank()) "Escojer portada" else "Cambiar portada")
                }

                AsyncImage(
                    model = Uri.parse(mentoringToStart.coverUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.FillWidth,
                    error = painterResource(id = R.drawable.pick_image),
                )
                
                SmallButton(onClick = { onEvent(HomeEvent.StartLiveMentoring(mentoringToStart)) }) {
                    Text(text = "Iniciar tutoría")
                }
                
            }
        }
    }

}