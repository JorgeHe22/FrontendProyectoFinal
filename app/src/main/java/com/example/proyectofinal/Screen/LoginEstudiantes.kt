package com.example.proyectofinal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            mensaje = null
        }
    }

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
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 36.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imagen decorativa o logo
                Icon(
                    imageVector = Icons.Default.School, // O reemplaza con painterResource(...)
                    contentDescription = "Icono estudiante",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(bottom = 8.dp)
                )

                Text(
                    text = "Iniciar Sesión - Estudiante",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                OutlinedTextField(
                    value = documento,
                    onValueChange = { documento = it },
                    label = { Text("Documento") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.large,
                    elevation = ButtonDefaults.buttonElevation(8.dp)
                ) {
                    Text("Iniciar Sesión")
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Volver")
                }
            }
        }
    }
}