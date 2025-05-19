package com.example.proyectofinal.Screen

import androidx.compose.foundation.Image
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

    // Cargar el dispositivo cuando esta pantalla aparece
    LaunchedEffect(Unit) {
        viewModel.obtenerDispositivo(usuario.id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Carnet Estudiantil", style = MaterialTheme.typography.headlineMedium)

        // ✅ Pasamos el viewModel a la función
        CarnetUsuario(usuario, viewModel)

        viewModel.dispositivoRegistrado?.let { dispositivo ->
            DispositivoCard(dispositivo)
        } ?: Text("No hay dispositivos registrados.")

        Button(
            onClick = { navController.navigate("accionesEstudiante") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver opciones")
        }

        OutlinedButton(
            onClick = { navController.popBackStack("menu", inclusive = false) },
            modifier = Modifier.fillMaxWidth()
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
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .border(2.dp, Color.Gray, RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_universidad),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(60.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // ✅ Extraer ID del dispositivo si existe
            val equipoId = viewModel.dispositivoRegistrado?.id ?: ""

            // ✅ Formato QR con usuarioId|equipoId
            val qrTexto = "${usuario.id}|$equipoId"
            val qrBitmap = remember(qrTexto) { generarCodigoQR(qrTexto) }
            val imageBitmap = remember(qrBitmap) { qrBitmap.asImageBitmap() }

            Spacer(modifier = Modifier.height(12.dp))
            Text("Código QR", style = MaterialTheme.typography.bodySmall)
            Image(
                bitmap = imageBitmap,
                contentDescription = "Código QR del estudiante",
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Divider()

            Text("Nombre: ${usuario.nombre}")
            Text("Documento: ${usuario.documento}")
            Text("Correo: ${usuario.correo}")
            Text("Carrera: ${usuario.carrera}")
            Text("Rol: ${usuario.rol}")
        }
    }
}
/*
fun convertirLinkDriveADirecto(link: String): String {
    val regex = Regex("""/d/([a-zA-Z0-9_-]+)""")
    val match = regex.find(link)
    val id = match?.groups?.get(1)?.value
    return if (id != null) {
        "https://drive.google.com/uc?export=view&id=$id"
    } else {
        link
    }
}
*/

@Composable
fun DispositivoCard(dispositivo: DispositivoRequest) {
    Text("Dispositivo Registrado", style = MaterialTheme.typography.headlineSmall)

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp)
            .border(2.dp, Color(0xFF6200EE), RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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

                val urlPrueba ="https://drive.google.com/uc?export=view&id=1mcQCULQit979vM1h3O-8am1WsouNaq3V"
                Image(
                    painter = rememberAsyncImagePainter(urlPrueba),
                    contentDescription = "Imagen del dispositivo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
            }
        }
    }
}



