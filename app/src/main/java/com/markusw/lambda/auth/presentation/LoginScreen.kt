package com.markusw.lambda.auth.presentation

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.identity.Identity
import com.markusw.lambda.R
import com.markusw.lambda.core.presentation.components.OutlinedButton
import com.markusw.lambda.core.utils.Result
import com.spr.jetpack_loading.components.indicators.LineSpinFadeLoaderIndicator
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onEvent: (LoginEvent) -> Unit,
    modifier: Modifier = Modifier,
    state: LoginState,
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
                return@rememberLauncherForActivityResult
            }
            onEvent(LoginEvent.FinishGoogleSignIn)
        }
    )

    Scaffold(
        modifier = modifier
    ) { innerPadding ->

        Box {
            Image(
                painter = painterResource(
                    id = R.drawable.top_ilustration
                ),
                contentDescription = null,
                modifier = Modifier
                    .scale(3.6f)
                    .offset(x = -10.dp)
            )

            Image(
                painter = painterResource(
                    id = R.drawable.bottom_ilustration
                ),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .scale(3.6f)
                    .offset(y = 10.dp)
            )

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.lambda_logo),
                        contentDescription = null,
                        modifier = Modifier.size(150.dp)
                    )
                    Text(
                        text = "¡Bienvenido a Lambda!",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        ),
                    )
                    Text(
                        text = "Transforma tus dudas en oportunidades de aprendizaje",
                        style = MaterialTheme.typography.titleMedium.copy(
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Light
                        ),
                    )

                    Spacer(Modifier.height(36.dp))

                    OutlinedButton(
                        enabled = !state.isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            coroutineScope.launch {
                                onEvent(LoginEvent.StartGoogleSignIn)
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        googleAuthUiClient.signIn() ?: return@launch
                                    ).build()
                                )
                            }
                        },
                        contentPadding = PaddingValues(
                            vertical = 14.dp,
                            horizontal = 16.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_google),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(text = "Continuar con Google", color = Color.Black)
                        }
                    }
                }
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x4D0D1117)),
                    contentAlignment = Alignment.Center
                ) {
                    LineSpinFadeLoaderIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(modifier: Modifier = Modifier) {
    LoginScreen(onEvent = {}, state = LoginState())
}
