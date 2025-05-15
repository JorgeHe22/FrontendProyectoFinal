package com.example.proyectofinal.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal.ViewModel.UsuarioViewModel

@Composable
fun AccionesEstudiante(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val usuario = viewModel.usuarioLogueado

    if (usuario == null) {
        Text("No se encontró información del usuario.")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hola, ${usuario.nombre}", style = MaterialTheme.typography.headlineMedium)

        // Botón para registrar dispositivo
        Button(
            onClick = { navController.navigate("registroDispositivo") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar Dispositivo")
        }

        // Otros posibles botones (futuro)
        //     Text("Ver historial de ingresos")
        // Botón para regresar al carnet
        OutlinedButton(
            onClick = { navController.popBackStack("perfilUsuario", inclusive = false) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al Carnet")
        }
    }
}
