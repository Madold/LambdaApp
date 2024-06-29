package com.markusw.lambda.core.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ExtraLargeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {

    Button(
        modifier = modifier,
        onClick = onClick,
        contentPadding = PaddingValues(vertical = 14.dp, horizontal = 42.dp),
        content = content
    )

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExtraLargeButtonPreview(modifier: Modifier = Modifier) {
    
    ExtraLargeButton(onClick = { /*TODO*/ }) {
        Text(text = "Login")
    }
    
}