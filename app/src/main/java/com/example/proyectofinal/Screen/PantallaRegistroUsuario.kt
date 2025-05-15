package com.example.proyectofinal.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
    var carrera by remember { mutableStateOf(TextFieldValue()) }
    var rol by remember { mutableStateOf(TextFieldValue()) }

    var expanded by remember { mutableStateOf(false) }
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
                .padding(16.dp)
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Registro de Usuario", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
            OutlinedTextField(value = documento, onValueChange = { documento = it }, label = { Text("Documento") })
            OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo") })
            OutlinedTextField(value = carrera, onValueChange = { carrera = it }, label = { Text("Carrera") })

            // Dropdown para seleccionar el rol
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
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
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    opcionesRol.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion.replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                rol = TextFieldValue(opcion)
                                expanded = false
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
                        carrera = carrera.text,
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar Usuario")
            }

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al Menú")
            }
        }
    }
}
