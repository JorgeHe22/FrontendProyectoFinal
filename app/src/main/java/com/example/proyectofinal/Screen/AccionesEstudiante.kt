package com.example.proyectofinal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFE6EEFF), Color.White)
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Saludo principal
            Text(
                text = "👋 Hola, ${usuario.nombre}",
                style = MaterialTheme.typography.headlineSmall
            )

            // Subtítulo con guía
            Text(
                text = "Selecciona una opción para continuar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // Botón: Registrar dispositivo
            Button(
                onClick = { navController.navigate("registroDispositivo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large,
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Registrar Dispositivo")
            }

            // Botón: Ver historial (por ejemplo, de ingresos/salidas)
            Button(
                onClick = {
                    val usuarioId = viewModel.usuarioLogueado?.id
                    navController.navigate("historial/$usuarioId")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Default.History, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ver Historial de Ingresos")
            }

            // Botón: Actualizar datos
            Button(
                onClick = { /* navController.navigate("actualizarDatos") */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Actualizar Mis Datos")
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Botón: Volver
            OutlinedButton(
                onClick = { navController.popBackStack("perfilUsuario", inclusive = false) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Volver al Carnet")
            }
        }
    }
}
