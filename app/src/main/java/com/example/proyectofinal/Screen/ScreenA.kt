package com.example.proyectofinal.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo_universidad),
            contentDescription = "Logo Universidad",
            modifier = Modifier
                .height(280.dp)
                .padding(bottom = 32.dp)
        )

        // Botón de Registro
        Button(
            onClick = { navController.navigate("registroUsuario") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de Iniciar Sesión (lleva a nueva pantalla de selección de rol)
        Button(
            onClick = { navController.navigate("seleccionLogin") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar Sesión")
        }
    }
}