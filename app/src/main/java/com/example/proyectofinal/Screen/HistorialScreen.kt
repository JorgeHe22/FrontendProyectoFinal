package com.example.proyectofinal.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectofinal.ViewModel.HistorialViewModel
import com.example.proyectofinal.utils.formatearFecha


@Composable
fun HistorialScreen(
    viewModel: HistorialViewModel,
    navController: NavController,
    usuarioId: String
) {
    val historial by viewModel.historial.collectAsState()
    var filtroSeleccionado by remember { mutableStateOf("Todos") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Historial de movimientos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 🧭 Menú de filtros
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Todos", "Entrada", "Salida").forEach { tipo ->
                val seleccionado = filtroSeleccionado == tipo
                Button(
                    onClick = { filtroSeleccionado = tipo },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (seleccionado)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surface
                    ),
                    elevation = ButtonDefaults.buttonElevation(if (seleccionado) 4.dp else 0.dp)
                ) {
                    Text(
                        tipo,
                        color = if (seleccionado)
                            Color.White
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // 🧩 Filtrado dinámico
        val historialFiltrado = when (filtroSeleccionado) {
            "Entrada" -> historial.filter { it.tipoMovimiento.equals("ENTRADA", ignoreCase = true) }
            "Salida" -> historial.filter { it.tipoMovimiento.equals("SALIDA", ignoreCase = true) }
            else -> historial
        }

        // 🧾 Lista de movimientos
        if (historialFiltrado.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay movimientos registrados.")
            }
        } else {
            LazyColumn {
                items(historialFiltrado) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Tipo: ${item.tipoMovimiento}", fontWeight = FontWeight.Bold)
                            Text("Fecha: ${formatearFecha(item.fechaHora)}")
                            Text("Equipo: ${item.equipo.marca ?: "Desconocido"} ${item.equipo.modelo ?: ""}")
                            Text("Serial: ${item.equipo.numeroSerie ?: "N/A"}")
                        }
                    }
                }
            }
        }
    }
}