package com.example.proyectofinal.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal.R
import androidx.compose.ui.graphics.Color

@Composable
fun SeleccionLoginScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Selecciona tu rol", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de Estudiante
        ElevatedButton(
            onClick = { navController.navigate("loginEstudiante") },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7B1FA2), // Morado
                contentColor = Color.White
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.icono_estudiante),
                contentDescription = "Icono estudiante",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Iniciar sesión como Estudiante")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de Guardia
        ElevatedButton(
            onClick = { navController.navigate("loginGuardia") },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7B1FA2), // Morado
                contentColor = Color.White
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.icono_guardia),
                contentDescription = "Icono guardia",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Iniciar sesión como Guardia")
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al Menú")
        }
    }
}