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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.ViewModel.HistorialViewModel


@Composable
fun HistorialScreen(
    viewModel: HistorialViewModel,
    usuarioId: String
) {
    val historial by viewModel.historial.collectAsState(initial = emptyList())

    // Cargar historial una sola vez
    LaunchedEffect(usuarioId) {
        viewModel.cargarHistorial(usuarioId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
            .padding(16.dp)
    ) {
        Text(
            text = "Historial de movimientos",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier
                .padding(bottom = 12.dp)
                .align(Alignment.CenterHorizontally)
        )

        if (historial.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No hay movimientos registrados.",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn {
                items(historial, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Tipo: ${item.tipoMovimiento}", fontWeight = FontWeight.SemiBold)
                            Text("Observación: ${item.observacion ?: "Sin descripción"}")
                            Text("Equipo: ${item.equipo.marca ?: "Desconocido"} ${item.equipo.modelo ?: ""}")
                            Text("Fecha: ${item.fechaHora ?: "Sin fecha"}")
                        }
                    }
                }
            }
        }
    }
}