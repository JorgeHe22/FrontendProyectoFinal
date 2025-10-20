package com.example.proyectofinal.Screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.proyectofinal.Model.DispositivoRequest
import com.example.proyectofinal.ViewModel.DispositivoViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActualizarEquipoScreen(
    navController: NavController,
    dispositivoViewModel: DispositivoViewModel
) {
    // 🧩 Obtenemos el equipo actual del ViewModel
    val equipoActual by dispositivoViewModel.equipoActualFlow.collectAsState()

    // 🖋️ Campos editables
    var marca by remember { mutableStateOf(equipoActual?.marca ?: "") }
    var modelo by remember { mutableStateOf(equipoActual?.modelo ?: "") }
    var serial by remember { mutableStateOf(equipoActual?.serial ?: "") }
    var fotoUrl by remember { mutableStateOf(equipoActual?.fotoUrl ?: "") }

    // 🧠 Estado del mensaje
    val mensaje by dispositivoViewModel.mensajeActualizacion.collectAsState(initial = "")
    var mostrarDialogoExito by remember { mutableStateOf(false) }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFFE6EEFF), Color.White)
                    )
                )
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 🟦 Título
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Actualizar Equipo",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                // 🧾 Tarjeta
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(8.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = marca,
                            onValueChange = { marca = it },
                            label = { Text("Marca del equipo") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = modelo,
                            onValueChange = { modelo = it },
                            label = { Text("Modelo") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = serial,
                            onValueChange = { serial = it },
                            label = { Text("Número de serie") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = fotoUrl,
                            onValueChange = { fotoUrl = it },
                            label = { Text("URL de la foto") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (fotoUrl.trim().startsWith("http")) {
                            Text(
                                text = "Vista previa de la imagen",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Image(
                                painter = rememberAsyncImagePainter(model = fotoUrl.trim()),
                                contentDescription = "Vista previa imagen",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .padding(top = 8.dp)
                            )
                        }
                    }
                }

                // 🟢 Botón Guardar cambios
                Button(
                    onClick = {
                        println("🖱 CLICK Guardar cambios")

                        val base = equipoActual
                        if (base == null) {
                            println("❌ equipoActual NULL, no puedo hacer PUT")
                            return@Button
                        }
                        if (base.id == null) {
                            println("❌ El equipo no tiene id, no puedo hacer PUT")
                            return@Button
                        }

                        val actualizado = DispositivoRequest(
                            id = base.id,
                            marca = marca.trim(),
                            modelo = modelo.trim(),
                            serial = serial.trim(),
                            fotoUrl = fotoUrl.trim(),
                            usuarioId = base.usuarioId
                        )

                        println("⏩ PUT id=${base.id} usuarioId=${base.usuarioId} datos=${actualizado.marca}/${actualizado.modelo}")
                        dispositivoViewModel.actualizarEquipo(base.id.toString(), actualizado)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text("Guardar cambios")
                }

                // 🔙 Botón Cancelar
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Cancelar")
                }

                // 📢 Mensaje de estado
                if (mensaje.isNotEmpty()) {
                    Text(
                        text = mensaje,
                        color = if (mensaje.startsWith("✅")) Color(0xFF4CAF50) else Color.Red,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // 🟢 Diálogo de éxito
            if (mensaje.startsWith("✅")) {
                LaunchedEffect(mensaje) {
                    delay(800)
                    mostrarDialogoExito = true
                }
            }

            if (mostrarDialogoExito) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogoExito = false },
                    confirmButton = {
                        TextButton(onClick = {
                            mostrarDialogoExito = false
                            navController.popBackStack("accionesEstudiante", inclusive = false)
                        }) {
                            Text("Aceptar")
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(36.dp)
                        )
                    },
                    title = { Text("¡Actualización exitosa!") },
                    text = { Text("Los datos del equipo se actualizaron correctamente.") }
                )
            }
        }
    }
}

