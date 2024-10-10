package com.markusw.lambda.core.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton as MaterialButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markusw.lambda.ui.theme.LambdaAppTheme

@Composable
fun OutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        containerColor = Color.White,
    ),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(
        pressedElevation = 0.dp,
        defaultElevation = 0.dp,
        disabledElevation = 0.dp
    ),
    border: BorderStroke = BorderStroke(
        width = 3.dp,
        color = Color(0xFFF2F4F5)
    ),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
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
fun OutlinedButtonPreview(modifier: Modifier = Modifier) {
    LambdaAppTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        OutlinedButton(
            onClick = {}
        ) {
            Text("Click me", color = Color.Black)
        }
    }
}