package com.markusw.lambda.home.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun JoiningLiveSessionDialog() {
    
    Card {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            CircularProgressIndicator()
            Text(text = "Uniendose a la sesión...")
        }
    }
    
}