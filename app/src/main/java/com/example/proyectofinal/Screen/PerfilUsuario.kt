package com.example.proyectofinal.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.proyectofinal.Model.DispositivoRequest
import com.example.proyectofinal.R
import com.example.proyectofinal.ViewModel.DispositivoViewModel
import com.example.proyectofinal.ViewModel.UsuarioViewModel
import com.example.proyectofinal.utils.generarCodigoQR

@Composable
fun PerfilUsuario(
    navController: NavController,
    viewModel: UsuarioViewModel,
    dispositivoViewModel: DispositivoViewModel
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
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Boolean>("equipoActualizado")
            ?.observe(lifecycleOwner) { actualizado ->
                if (actualizado) {
                    viewModel.obtenerDispositivo(usuario.id)
                }
            }
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

// ✅ ViewModel de Dispositivo (para obtener el equipo actualizado)
        val dispFactory = remember {
            com.example.proyectofinal.ViewModel.DispositivoVMFactory(
                com.example.proyectofinal.Network.RetrofitClient.apiService
            )
        }
        val dispVM: com.example.proyectofinal.ViewModel.DispositivoViewModel =
            androidx.lifecycle.viewmodel.compose.viewModel(factory = dispFactory)

// ✅ Escucha cambios del equipo actualizado
        val equipoActualizado by dispVM.equipoActualFlow.collectAsState()

// ✅ Usa el dispositivo guardado o el actualizado
        val dispositivoRender = remember(viewModel.dispositivoRegistrado, equipoActualizado) {
            val base = viewModel.dispositivoRegistrado ?: equipoActualizado
            when (base) {
                is com.example.proyectofinal.Model.DispositivoRequest -> base
                is com.example.proyectofinal.Model.EquipoRequest -> com.example.proyectofinal.Model.DispositivoRequest(
                    id = base.id?.let { java.util.UUID.fromString(it) },
                    marca = base.marca,
                    modelo = base.modelo,
                    serial = base.serial,
                    fotoUrl = base.foto,
                    usuarioId = viewModel.usuarioLogueado?.id ?: "" // ✅ pasa el id del usuario logueado
                )
                else -> null
            }
        }

// ✅ Render del Card
        if (dispositivoRender != null) {
            DispositivoCard(dispositivoRender)
        } else {
            Text("No hay dispositivos registrados.")
        }

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



