package com.markusw.lambda.home.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.presentation.components.ExtraSmallButton
import com.markusw.lambda.home.presentation.HomeEvent

@Composable
fun TutoringRequestCard(
    mentoring: Mentoring,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        ,
        horizontalArrangement = Arrangement.Center
    ) {

        Card(
            modifier = modifier
                .border(
                    color = Color.Gray,
                    shape = RoundedCornerShape(
                        size = 8.dp
                    ),
                    width = 1.dp
                ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(
                size = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = mentoring.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Text(text = buildAnnotatedString {
                    append("Solicitada por: ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(mentoring.requester.displayName)
                    }
                }, style = MaterialTheme.typography.bodyMedium)

                Text(
                    text = mentoring.requesterDescription,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ExtraSmallButton(onClick = {
                        onEvent(HomeEvent.ChangeSelectedMentoring(mentoring))
                        onEvent(HomeEvent.ChangeProvideMentoringDialogVisibility(true))
                    }) {
                        Text(text = "Brindar tutoría")
                    }
                }
            }
        }
    }
}