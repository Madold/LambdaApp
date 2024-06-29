package com.markusw.lambda.core.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LargeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {

    Button(
        onClick = onClick,
        modifier = modifier,
        content = content,
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 36.dp)
    )

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LargeButtonPreview(modifier: Modifier = Modifier) {
    LargeButton(onClick = { /*TODO*/ }) {
        Text(text = "Start learning")
    }
}