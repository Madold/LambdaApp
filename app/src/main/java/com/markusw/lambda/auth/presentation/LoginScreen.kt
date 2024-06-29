package com.markusw.lambda.auth.presentation

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.identity.Identity
import com.markusw.lambda.R
import com.markusw.lambda.core.presentation.components.EmailField
import com.markusw.lambda.core.presentation.components.LargeButton
import com.markusw.lambda.core.presentation.components.PasswordField
import com.markusw.lambda.core.utils.Result
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onEvent: (LoginEvent) -> Unit,
    modifier: Modifier = Modifier,
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val googleAuthUiClient = remember {
        GoogleAuthUiClient(
            context,
            Identity.getSignInClient(context)
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                coroutineScope.launch {
                    when (val authResult = googleAuthUiClient.getAuthCredentialFromIntent(
                        result.data ?: return@launch
                    )) {
                        is Result.Error -> {
                            Toast.makeText(
                                context,
                                authResult.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is Result.Success -> {
                            onEvent(
                                LoginEvent.OnGoogleSignInResult(
                                    authResult.data ?: return@launch
                                )
                            )
                        }
                    }
                }
            }
        }
    )

    Scaffold(
        modifier = modifier
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier.weight(1.5f),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.lambda_logo),
                    contentDescription = null
                )
                Text(
                    text = "Iniciar sesión",
                    style = MaterialTheme.typography.titleLarge
                )
                EmailField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth()
                )
                PasswordField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(text = buildAnnotatedString {
                        append("¿No tienes una cuenta? ")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("Registrate aquí")
                        }
                    })
                }
                Spacer(modifier = Modifier.height(16.dp))
                LargeButton(onClick = { /*TODO*/ }) {
                    Text(text = "Iniciar sesión")
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(text = "O inicia sesión con")
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    onClick = {
                        coroutineScope.launch {
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    googleAuthUiClient.signIn() ?: return@launch
                                ).build()
                            )
                        }
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(modifier: Modifier = Modifier) {
    LoginScreen(onEvent = {})
}
