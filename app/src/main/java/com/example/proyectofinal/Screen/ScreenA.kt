package com.example.proyectofinal.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal.R
import com.example.proyectofinal.ViewModel.UsuarioViewModel

@Composable
fun ScreenA(navController: NavController, viewModel: UsuarioViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFE6EEFF), Color.White)
                )
            )
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo institucional
        Image(
            painter = painterResource(id = R.drawable.logo_universidad),
            contentDescription = "Logo Universidad",
            modifier = Modifier
                .height(220.dp)
                .padding(bottom = 24.dp)
        )

        // Slogan institucional (opcional)
        Text(
            text = "Educación de calidad al alcance de todos",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Botón de registro
        Button(
            onClick = { navController.navigate("registroUsuario") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text(text = "Registrarse", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de inicio de sesión
        OutlinedButton(
            onClick = { navController.navigate("seleccionLogin") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text(text = "Iniciar Sesión", style = MaterialTheme.typography.titleMedium)
        }
    }
}