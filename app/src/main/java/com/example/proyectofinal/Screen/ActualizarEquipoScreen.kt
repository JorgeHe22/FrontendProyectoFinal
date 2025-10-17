package com.example.proyectofinal.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal.Model.EquipoRequest
import com.example.proyectofinal.ViewModel.DispositivoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActualizarEquipoScreen(
    navController: NavController,
    dispositivoViewModel: DispositivoViewModel
) {
    // 🧩 Campos controlados por estado Compose
    var marca by remember { mutableStateOf(TextFieldValue("")) }
    var modelo by remember { mutableStateOf(TextFieldValue("")) }
    var serial by remember { mutableStateOf(TextFieldValue("")) }
    var fotoUrl by remember { mutableStateOf(TextFieldValue("")) }

    // 🧠 Mensaje del ViewModel (Flow → Compose)
    val mensajeActualizacion by dispositivoViewModel.mensajeActualizacion.collectAsState(initial = "")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Actualizar Equipo",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🖋️ Campos de texto estilizados
            OutlinedTextField(
                value = marca,
                onValueChange = { marca = it },
                label = { Text("Marca del equipo") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            )

            OutlinedTextField(
                value = modelo,
                onValueChange = { modelo = it },
                label = { Text("Modelo del equipo") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            )

            OutlinedTextField(
                value = serial,
                onValueChange = { serial = it },
                label = { Text("Número de serie") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            )

            OutlinedTextField(
                value = fotoUrl,
                onValueChange = { fotoUrl = it },
                label = { Text("URL de la foto del equipo") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            )

            // 🟢 Botón para actualizar
            Button(
                onClick = {
                    val equipoActualizado = EquipoRequest(
                        marca = marca.text,
                        modelo = modelo.text,
                        serial = serial.text,
                        foto = fotoUrl.text
                    )
                    dispositivoViewModel.actualizarEquipo(
                        id = "cc7462b1-9023-4905-a59b-55de7752d202", // ⚠️ Reemplaza con el ID real del equipo del estudiante
                        equipo = equipoActualizado
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar cambios", style = MaterialTheme.typography.bodyLarge)
            }

            // 🟣 Mensaje de estado
            if (mensajeActualizacion.isNotEmpty()) {
                val esExitoso = mensajeActualizacion.startsWith("✅")
                val color = if (esExitoso)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (esExitoso) Icons.Default.Check else Icons.Default.Edit,
                        contentDescription = null,
                        tint = color
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = mensajeActualizacion,
                        color = color,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // 🔙 Botón para volver
            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Volver", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}