package com.example.proyectofinal.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal.ViewModel.UsuarioViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginEstudiantes(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    var documento by remember { mutableStateOf(TextFieldValue()) }
    var correo by remember { mutableStateOf(TextFieldValue()) }
    val snackbarHostState = remember { SnackbarHostState() }
    var mensaje by remember { mutableStateOf<String?>(null) }

    // Mostrar Snackbar al cambiar el mensaje
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
                .padding(16.dp)
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Iniciar Sesión - Estudiante", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = documento,
                onValueChange = { documento = it },
                label = { Text("Documento") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.loginEstudiante(
                        documento = documento.text,
                        correo = correo.text
                    ) { exito ->
                        if (exito) {
                            navController.navigate("perfilUsuario")
                        } else {
                            mensaje = "Credenciales incorrectas"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}