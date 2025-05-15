package com.example.proyectofinal.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal.ViewModel.UsuarioViewModel

@Composable
fun EscogerMovimientos(navController: NavController, viewModel: UsuarioViewModel) {
    var tipoMovimiento by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Seleccione el tipo de movimiento", style = MaterialTheme.typography.headlineMedium)

        Button(
            onClick = {
                tipoMovimiento = "entrada"
                navController.navigate("escanearQR/$tipoMovimiento")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar Entrada")
        }

        Button(
            onClick = {
                tipoMovimiento = "salida"
                navController.navigate("escanearQR/$tipoMovimiento")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar Salida")
        }
    }
}
