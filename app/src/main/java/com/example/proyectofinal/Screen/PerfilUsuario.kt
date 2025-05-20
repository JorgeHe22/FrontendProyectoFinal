package com.example.proyectofinal.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.proyectofinal.R
import com.example.proyectofinal.ViewModel.UsuarioViewModel
import com.example.proyectofinal.Model.DispositivoRequest
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectofinal.utils.generarCodigoQR

@Composable
fun PerfilUsuario(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val usuario = viewModel.usuarioLogueado
    val scrollState = rememberScrollState()

    if (usuario == null) {
        Text("No hay usuario logueado.")
        return
    }

    LaunchedEffect(Unit) {
        viewModel.obtenerDispositivo(usuario.id)
    }

    Column(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFE6EEFF), Color.White)
                )
            )
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Carnet Estudiantil", style = MaterialTheme.typography.headlineMedium)

        CarnetUsuario(usuario, viewModel)

        Divider(thickness = 1.dp)

        Text("Dispositivo Registrado", style = MaterialTheme.typography.headlineSmall)

        viewModel.dispositivoRegistrado?.let { dispositivo ->
            DispositivoCard(dispositivo)
        } ?: Text("No hay dispositivos registrados.")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("accionesEstudiante") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Ver opciones")
        }

        OutlinedButton(
            onClick = { navController.popBackStack("menu", inclusive = false) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Cerrar sesión")
        }
    }
}

@Composable
fun CarnetUsuario(
    usuario: com.example.proyectofinal.Model.UsuarioResponse,
    viewModel: UsuarioViewModel
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_universidad),
                contentDescription = "Logo Uniminuto",
                modifier = Modifier.height(50.dp)
            )

            val equipoId = viewModel.dispositivoRegistrado?.id ?: ""
            val qrTexto = "${usuario.id}|$equipoId"
            val qrBitmap = remember(qrTexto) { generarCodigoQR(qrTexto) }
            val imageBitmap = remember(qrBitmap) { qrBitmap.asImageBitmap() }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Código QR", style = MaterialTheme.typography.labelMedium)
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Código QR del estudiante",
                    modifier = Modifier.size(160.dp)
                )
            }

            Divider()

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("Nombre: ${usuario.nombre}")
                Text("Documento: ${usuario.documento}")
                Text("Correo: ${usuario.correo}")
                Text("Carrera: ${usuario.carrera}")
                Text("Rol: ${usuario.rol}")
            }
        }
    }
}

@Composable
fun DispositivoCard(dispositivo: DispositivoRequest) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Marca: ${dispositivo.marca}")
            Text("Modelo: ${dispositivo.modelo}")
            Text("Serial: ${dispositivo.serial}")

            dispositivo.fotoUrl?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Imagen del dispositivo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(top = 8.dp)
                )
            }
        }
    }
}



