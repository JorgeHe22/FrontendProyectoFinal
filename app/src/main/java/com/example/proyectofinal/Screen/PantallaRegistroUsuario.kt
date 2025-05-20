package com.example.proyectofinal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal.Model.UsuarioRequest
import com.example.proyectofinal.ViewModel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistroUsuario(
    navController: NavController,
    viewModel: UsuarioViewModel,
    onRegistroExitoso: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var mensaje by remember { mutableStateOf<String?>(null) }

    var nombre by remember { mutableStateOf(TextFieldValue()) }
    var documento by remember { mutableStateOf(TextFieldValue()) }
    var correo by remember { mutableStateOf(TextFieldValue()) }
    var carreraSeleccionada by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf(TextFieldValue()) }

    // Dropdowns
    var expandedCarrera by remember { mutableStateOf(false) }
    var expandedRol by remember { mutableStateOf(false) }

    val opcionesCarrera = listOf(
        "ADMINISTRACIÓN DE EMPRESAS",
        "INGENIERÍA CIVIL",
        "INGENIERÍA DE SISTEMAS",
        "INGENIERÍA INDUSTRIAL",
        "INGENIERÍA AGROECOLÓGICA",
        "TRABAJO SOCIAL",
        "COMUNICACIÓN SOCIAL Y PERIODISMO",
        "LICENCIATURA EN EDUCACIÓN INFANTIL",
        "CONTADURÍA PÚBLICA",
        "ADMINISTRACIÓN EN SEGURIDAD Y SALUD EN EL TRABAJO"
    )

    val opcionesRol = listOf("estudiante", "guardia")
    val rolSeleccionado = rol.text.ifEmpty { "Selecciona un rol" }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            mensaje = null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFFE6EEFF), Color.White)
                    )
                )
                .padding(24.dp)
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Registro de Usuario",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

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

            // Dropdown para carreras
            ExposedDropdownMenuBox(
                expanded = expandedCarrera,
                onExpandedChange = { expandedCarrera = !expandedCarrera }
            ) {
                OutlinedTextField(
                    value = carreraSeleccionada,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Carrera") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedCarrera,
                    onDismissRequest = { expandedCarrera = false }
                ) {
                    opcionesCarrera.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                carreraSeleccionada = opcion
                                expandedCarrera = false
                            }
                        )
                    }
                }
            }

            // Dropdown para roles
            ExposedDropdownMenuBox(
                expanded = expandedRol,
                onExpandedChange = { expandedRol = !expandedRol }
            ) {
                OutlinedTextField(
                    value = rolSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Rol") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedRol,
                    onDismissRequest = { expandedRol = false }
                ) {
                    opcionesRol.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion.replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                rol = TextFieldValue(opcion)
                                expandedRol = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    val usuario = UsuarioRequest(
                        nombre = nombre.text,
                        documento = documento.text,
                        correo = correo.text,
                        carrera = carreraSeleccionada,
                        rol = rol.text
                    )
                    viewModel.registrarUsuario(usuario) { exito ->
                        mensaje = if (exito) {
                            onRegistroExitoso()
                            "Usuario registrado exitosamente"
                        } else {
                            "Error al registrar usuario"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Registrar Usuario", style = MaterialTheme.typography.titleMedium)
            }

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Volver al Menú", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
