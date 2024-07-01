package com.markusw.lambda.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.markusw.lambda.R
import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.presentation.components.SmallButton
import com.markusw.lambda.home.presentation.DonationDialog
import com.markusw.lambda.home.presentation.HomeEvent

@Composable
fun LiveMentoringCard(
    mentoring: Mentoring,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    var isDonationDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val buttonText = remember(mentoring.price) {
        if (mentoring.price == 0L) {
            "Unirse: GRATIS"
        } else {
            "Unirse COP: $${mentoring.price}"
        }
    }

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
        ,
    ) {
        Card(
            modifier = modifier
        ) {
            Column {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(mentoring.coverUrl)
                        .crossfade(true)
                        .build()
                    ,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )
                Column(
                    modifier = modifier.padding(horizontal = 8.dp)
                ) {

                    Text(
                        text = mentoring.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Text(text = mentoring.description)


                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Dictada por: ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                ),
                            ) {
                                append(mentoring.author?.displayName)
                            }
                        })

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(color = Color.Red)
                            )

                            Icon(
                                painter = painterResource(id = R.drawable.ic_user),
                                contentDescription = null
                            )

                            Text(text = mentoring.participants.size.toString())
                        }

                    }

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                        ,
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        
                        SmallButton(
                            onClick = { isDonationDialogVisible = true },
                            colors = ButtonDefaults.outlinedButtonColors()
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_donation),
                                contentDescription = null
                            )
                            Text(text = "Donar")
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp) )
                        
                        SmallButton(onClick = {

                        }) {
                            Text(buttonText)
                        }
                    }

                }
            }
        }
    }

    if (isDonationDialogVisible) {
        DonationDialog(
            mentoring = mentoring,
            onEvent = onEvent,
            onDismissRequest = { isDonationDialogVisible = false }
        )
    }

}