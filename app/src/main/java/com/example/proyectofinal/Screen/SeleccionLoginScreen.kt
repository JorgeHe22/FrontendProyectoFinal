package com.example.proyectofinal.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFE6EEFF), Color.White)
                )
            )
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen decorativa
        Image(
            painter = painterResource(id = R.drawable.logo_universidad),
            contentDescription = "Logo Uniminuto",
            modifier = Modifier
                .height(150.dp)
                .padding(bottom = 24.dp)
        )

        // Título
        Text(
            text = "Selecciona tu rol",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Botón Estudiante
        Button(
            onClick = { navController.navigate("loginEstudiante") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.icono_estudiante),
                contentDescription = "Icono estudiante",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Iniciar sesión como Estudiante")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón Guardia
        Button(
            onClick = { navController.navigate("loginGuardia") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.icono_guardia),
                contentDescription = "Icono guardia",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Iniciar sesión como Guardia")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón Volver
        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Volver al Menú")
        }
    }
}