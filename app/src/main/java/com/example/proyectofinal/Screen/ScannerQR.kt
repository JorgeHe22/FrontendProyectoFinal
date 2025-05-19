package com.example.proyectofinal.Screen

import android.Manifest
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectofinal.ViewModel.UsuarioViewModel
import com.example.proyectofinal.utils.hasPermission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EscanearQRScreen(
    navController: NavController,
    tipoMovimiento: String,
    viewModel: UsuarioViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val coroutineScope = rememberCoroutineScope()
    val qrDetectado = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Escaneando QR para registrar: $tipoMovimiento")

            if (cameraPermissionState.hasPermission && !qrDetectado.value) {
                AndroidView(factory = { context ->
                    val previewView = androidx.camera.view.PreviewView(context)
                    val preview = androidx.camera.core.Preview.Builder().build()
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    val imageAnalyzer = ImageAnalysis.Builder().build().also {
                        it.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                            processImageProxy(
                                imageProxy,
                                tipoMovimiento,
                                viewModel,
                                qrDetectado,
                                snackbarHostState,
                                navController,
                                coroutineScope
                            )
                        }
                    }

                    val cameraProvider = cameraProviderFuture.get()
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalyzer
                    )
                    preview.setSurfaceProvider(previewView.surfaceProvider)
                    previewView
                }, modifier = Modifier.fillMaxHeight(0.6f))
            } else if (!cameraPermissionState.hasPermission) {
                Text("Permiso de cámara denegado.")
            }
        }
    }
}

fun processImageProxy(
    imageProxy: ImageProxy,
    tipoMovimiento: String,
    viewModel: UsuarioViewModel,
    qrDetectado: MutableState<Boolean>,
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    coroutineScope: CoroutineScope
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null && !qrDetectado.value) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        val scanner = BarcodeScanning.getClient()

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                if (barcodes.isNotEmpty()) {
                    val codigoQR = barcodes.first().rawValue
                    qrDetectado.value = true

                    codigoQR?.let { codigo ->
                        val partes = codigo.split("|")
                        if (partes.size == 2) {
                            val usuarioId = partes[0]
                            val equipoId = partes[1]

                            viewModel.registrarMovimientoDesdeQR(usuarioId, equipoId, tipoMovimiento) { respuesta ->
                                coroutineScope.launch {
                                    if (respuesta != null) {
                                        val mensaje = """
                                            ✅ Movimiento registrado:

                                            👤 Usuario: ${respuesta.nombre}
                                            🪪 Documento: ${respuesta.documento}
                                            💻 Equipo: ${respuesta.equipo}
                                            🎓 Carrera: ${respuesta.carrera}
                                            📅 Fecha: ${respuesta.fechaHora}
                                            🔁 Tipo: ${respuesta.tipoMovimiento}
                                        """.trimIndent()

                                        snackbarHostState.showSnackbar(mensaje)
                                    } else {
                                        snackbarHostState.showSnackbar("❌ Error al registrar el movimiento.")
                                    }

                                    navController.popBackStack("menu", inclusive = false)
                                }
                            }
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("⚠️ QR inválido")
                                navController.popBackStack("menu", inclusive = false)
                            }
                        }
                    }
                }
            }
            .addOnFailureListener {
                Log.e("QRScanner", "Error escaneando: ${it.message}")
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}