package com.example.proyectofinal.Screen

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.ViewModel.HistorialViewModel



@Composable
fun HistorialScreen(
    viewModel: HistorialViewModel,
    usuarioId: String
) {
    val historialState = viewModel.historial.collectAsState()
    val historial = historialState.value

    LaunchedEffect(Unit) {
        viewModel.cargarHistorial(usuarioId)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Historial de movimientos",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyColumn {
            items(historial) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Tipo: ${item.tipoMovimiento}")
                        Text("Equipo: ${item.equipo.marca ?: "Desconocido"} ${item.equipo.modelo ?: ""}")
                        Text("Fecha: ${item.fechaHora ?: "Sin fecha"}")
                        item.observacion?.let { Text("Obs: $it") }
                    }
                }
            }
        }
    }
}