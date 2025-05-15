package com.example.proyectofinal.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var mensaje by remember { mutableStateOf<String?>(null) }

    // Mostrar Snackbar si hay mensaje
    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            mensaje = null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Registrar Dispositivo", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = marca,
                onValueChange = { marca = it },
                label = { Text("Marca") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = modelo,
                onValueChange = { modelo = it },
                label = { Text("Modelo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = serial,
                onValueChange = { serial = it },
                label = { Text("Serial") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fotoUrl,
                onValueChange = { fotoUrl = it },
                label = { Text("Foto URL del dispositivo") },
                modifier = Modifier.fillMaxWidth()
            )
            if (fotoUrl.trim().startsWith("http")) {
                Text("Vista previa de la imagen", style = MaterialTheme.typography.bodySmall)

                Image(
                    painter = rememberAsyncImagePainter(model = fotoUrl.trim()),
                    contentDescription = "Vista previa imagen",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
            }

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
                        mensaje = if (exito) {
                            navController.popBackStack("accionesEstudiante", inclusive = false)
                            "Dispositivo registrado correctamente"
                        } else {
                            "Error al registrar dispositivo"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }

    }


}