package com.example.proyectofinal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.ViewModel.HistorialViewModel


@Composable
fun HistorialScreen(
    viewModel: HistorialViewModel,
    usuarioId: String
) {
    val historial by viewModel.historial.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarHistorial(usuarioId)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text(
            text = "Historial de movimientos",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (historial.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay movimientos registrados.")
            }
        } else {
            LazyColumn {
                items(historial) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Tipo: ${item.tipoMovimiento}", fontWeight = FontWeight.Bold)
                            Text("Fecha: ${item.fechaHora?.replace("T", " ")?.substring(0, 16) ?: "Sin fecha"}")
                            Text("Equipo: ${item.equipo.marca} ${item.equipo.modelo}")
                            Text("Serial: ${item.equipo.serial}")
                            item.observacion?.let {
                                Text("Obs: $it", fontStyle = FontStyle.Italic)
                            }
                        }
                    }
                }
            }
        }
    }
}