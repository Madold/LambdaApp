package com.markusw.lambda.core.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SmallButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {

    Button(
        onClick = onClick,
        modifier = modifier,
        content = content,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        colors = colors,
        enabled = enabled
    )

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SmallButtonPreview(modifier: Modifier = Modifier) {
    SmallButton(onClick = { /*TODO*/ }) {
        Text(text = "+ Add")
    }
}