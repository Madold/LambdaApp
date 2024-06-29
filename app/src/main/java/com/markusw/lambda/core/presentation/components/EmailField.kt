package com.markusw.lambda.core.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.markusw.lambda.R

@Composable
fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        trailingIcon = {
            Icon(painter = painterResource(id = R.drawable.ic_email), contentDescription = null)
        },
        label = {
            Text(text = "Correo")
        }
    )

}