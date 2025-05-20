package com.example.proyectofinal.Screen

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.navigation.NavController
import com.example.proyectofinal.ViewModel.UsuarioViewModel
import kotlinx.coroutines.delay

@Composable
fun EscogerMovimientos(navController: NavController, viewModel: UsuarioViewModel) {
    val context = LocalContext.current
    val vibrator = remember { context.getSystemService<Vibrator>() }

    var tipoMovimiento by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }

    // Fondo degradado top-down
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFE6EEFF), Color.White)
                )
            )
            .padding(horizontal = 24.dp, vertical = 36.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título elegante
            Text(
                text = "📍 Tipo de Movimiento",
                style = MaterialTheme.typography.headlineSmall
            )

            // Botón entrada
            BotonAnimadoConHaptic(
                texto = "Registrar Entrada",
                icono = Icons.Default.Login,
                color = MaterialTheme.colorScheme.primary
            ) {
                tipoMovimiento = "entrada"
                vibrar(vibrator)
                navController.navigate("escanearQR/$tipoMovimiento")
            }

            // Botón salida
            BotonAnimadoConHaptic(
                texto = "Registrar Salida",
                icono = Icons.Default.Logout,
                color = MaterialTheme.colorScheme.secondary
            ) {
                tipoMovimiento = "salida"
                vibrar(vibrator)
                navController.navigate("escanearQR/$tipoMovimiento")
            }
        }
    }
}

// 🚀 BOTÓN PERSONALIZADO con ícono + vibración + animación de escala
@Composable
fun BotonAnimadoConHaptic(
    texto: String,
    icono: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (pressed) 0.97f else 1f, label = "")

    Button(
        onClick = {
            pressed = true
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        elevation = ButtonDefaults.buttonElevation(6.dp)
    ) {
        Icon(icono, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(texto)
    }

    // Reset de animación
    LaunchedEffect(pressed) {
        if (pressed) {
            delay(150)
            pressed = false
        }
    }
}

// 🔊 Vibración suave al presionar
fun vibrar(vibrator: Vibrator?) {
    vibrator?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            it.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            it.vibrate(40)
        }
    }
}

