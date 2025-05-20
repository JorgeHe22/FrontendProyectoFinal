package com.example.proyectofinal.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.proyectofinal.Model.DispositivoRequest
import com.example.proyectofinal.ViewModel.UsuarioViewModel
import com.example.proyectofinal.utils.convertirLinkDriveADirecto

@Composable
fun RegistroDispositivo(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val usuario = viewModel.usuarioLogueado

    if (usuario == null) {
        Text("Usuario no disponible")
        return
    }

    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var serial by remember { mutableStateOf("") }
    var fotoUrl by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    var mostrarDialogoExito by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
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
                // Ícono + título
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Laptop,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Registrar Dispositivo",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                // Tarjeta decorativa
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
                            label = { Text("Marca del dispositivo") },
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
                            label = { Text("Serial") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = fotoUrl,
                            onValueChange = { fotoUrl = it },
                            label = { Text("Enlace de foto (opcional)") },
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

                // Botón Registrar
                Button(
                    onClick = {
                        val request = DispositivoRequest(
                            marca = marca,
                            modelo = modelo,
                            serial = serial,
                            fotoUrl = convertirLinkDriveADirecto(fotoUrl),
                            usuarioId = usuario.id
                        )

                        viewModel.registrarDispositivo(request) { exito ->
                            if (exito) {
                                mostrarDialogoExito = true
                            } else {
                                var mensaje = "Error al registrar dispositivo"
                            }
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.large,
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Text("Registrar")
                }

                // Botón Cancelar
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Cancelar")
                }
            }
        }

        // ✅ Diálogo de éxito con ícono
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
                title = { Text("¡Registro exitoso!") },
                text = { Text("Tu dispositivo ha sido guardado correctamente.") }
            )
        }
    }
}