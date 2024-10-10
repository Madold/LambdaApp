package com.markusw.lambda.core.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markusw.lambda.ui.theme.LambdaAppTheme
import androidx.compose.material3.Button as MaterialButton

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(
        defaultElevation = 0.dp,
        pressedElevation = 0.dp,
        disabledElevation = 0.dp
    ),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = PaddingValues(
        start = 24.dp,
        top = 16.dp,
        end = 24.dp,
        bottom = 16.dp
    ),
    content: @Composable RowScope.() -> Unit
) {

    MaterialButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        content = content
    )
}

@Preview(
    showBackground = true
)
@Composable
fun ButtonPreview(modifier: Modifier = Modifier) {
    LambdaAppTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        Button(
            onClick = {}
        ) {
            Text("Click me")
        }
    }
}