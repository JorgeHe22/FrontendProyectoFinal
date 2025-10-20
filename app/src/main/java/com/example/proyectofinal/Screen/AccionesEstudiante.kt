package com.example.proyectofinal.Screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectofinal.Model.EquipoRequest
import com.example.proyectofinal.Network.RetrofitClient
import com.example.proyectofinal.ViewModel.DispositivoVMFactory
import com.example.proyectofinal.ViewModel.DispositivoViewModel
import com.example.proyectofinal.ViewModel.UsuarioViewModel



@Composable
fun AccionesEstudiante(
    navController: NavController,
    viewModel: UsuarioViewModel,
    dispositivoViewModel: DispositivoViewModel // ✅ Este viene desde MainActivity
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
            Text(
                text = "👋 Hola, ${usuario.nombre}",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Selecciona una opción para continuar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

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

            Button(
                onClick = {
                    val usuarioId = viewModel.usuarioLogueado?.id
                    navController.navigate("historial/$usuarioId")
                    Log.d("HistorialDebug", "Enviando ID: $usuarioId")
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

            // 🟩 Botón: Actualizar datos
            Button(
                onClick = {
                    val disp = viewModel.dispositivoRegistrado
                    if (disp == null) {
                        println("⚠️ No hay dispositivoRegistrado en UsuarioViewModel")
                        return@Button
                    }

                    println("📦 Seteando equipo actual -> ${disp.id}")
                    dispositivoViewModel.setEquipoActual(disp)

                    navController.navigate("actualizarEquipo")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Actualizar Equipo")
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

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
