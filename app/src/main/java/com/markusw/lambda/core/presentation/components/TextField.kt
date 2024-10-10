package com.markusw.lambda.core.presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markusw.lambda.ui.theme.LambdaAppTheme
import androidx.compose.material3.OutlinedTextField as MaterialTextField

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    labelText: String? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.Transparent,
        focusedContainerColor = Color(0xFFF2F4F5),
        unfocusedContainerColor = Color(0xFFF2F4F5),
        unfocusedBorderColor = Color.Transparent
    )
) {

    Column(
        verticalArrangement = Arrangement.Center
    ) {
        labelText?.let {
            Row {
                Spacer(Modifier.width(16.dp))
                Text(it, modifier = Modifier.alpha(0.4f))
            }
        }
        MaterialTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            shape = RoundedCornerShape(14.dp),
            visualTransformation = visualTransformation,
            singleLine = singleLine,
            colors = colors,
            interactionSource = interactionSource,
            maxLines = maxLines,
            minLines = minLines,
            isError = isError,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            supportingText = supportingText,
            placeholder = placeholder,
            label = {},
            prefix = prefix,
            suffix = suffix,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TextFieldPreview(modifier: Modifier = Modifier) {
    LambdaAppTheme(
        dynamicColor = false
    ) {
        TextField(
            value = "CHILLA",
            onValueChange = {},
            labelText = "Meate",
            leadingIcon = {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
            }
        )
    }
}