@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.lambda.home.presentation.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.markusw.lambda.R
import com.markusw.lambda.core.presentation.components.LargeButton
import com.markusw.lambda.core.utils.ext.pop
import com.markusw.lambda.ui.theme.LambdaAppTheme

@Composable
fun ShoppingView(
    homeNavController: NavController
) {
    Surface(
        color = MaterialTheme.colorScheme.primary
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {  },
                    navigationIcon = {
                        IconButton(onClick = { homeNavController.pop() }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null)
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Obtén premium",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Deshazte de los molestos anuncios y contribuye al crecimiento de la aplicación",
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                Image(
                    painter = painterResource(id = R.drawable.discount_illustration),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Anual",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Primeros 30 días gratis - Después $100.000/año", fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Mensual",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Primeros 7 días gratis - Después $10.000/mes", fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                LargeButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "Iniciar prueba de 7 días")
                }

            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShoppingViewPreview(modifier: Modifier = Modifier) {
    LambdaAppTheme {
        ShoppingView(homeNavController = rememberNavController())
    }
}