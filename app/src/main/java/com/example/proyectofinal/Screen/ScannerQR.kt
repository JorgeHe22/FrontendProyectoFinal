package com.example.proyectofinal.Screen

import android.Manifest
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectofinal.ViewModel.UsuarioViewModel
import com.example.proyectofinal.utils.hasPermission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.camera.core.ImageProxy

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EscanearQRScreen(
    navController: NavController,
    tipoMovimiento: String,
    viewModel: UsuarioViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        escaneoBloqueado = false
        ultimaLecturaExitosa = 0
        Log.d("ScannerQR", "🚀 Escáner reiniciado correctamente")
    }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val coroutineScope = rememberCoroutineScope()
    var qrDetectado by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    DisposableEffect(Unit) {
        onDispose { cameraProviderFuture.get().unbindAll() }
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

            if (permissionState.hasPermission && !qrDetectado) {
                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx)
                        val executor = ContextCompat.getMainExecutor(ctx)

                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()

                            // Preview
                            val previewUseCase = Preview.Builder()
                                .build()
                                .also { it.setSurfaceProvider(previewView.surfaceProvider) }

                            // Análisis de imagen
                            val analysisUseCase = ImageAnalysis.Builder()
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()
                                .also { analysis ->
                                    analysis.setAnalyzer(executor) { imageProxy ->
                                        processImageProxy(
                                            imageProxy,
                                            tipoMovimiento,
                                            viewModel,
                                            onQrDetected = { qrDetectado = it },
                                            snackbarHostState,
                                            navController,
                                            coroutineScope
                                        )
                                    }
                                }

                            // Vincular casos de uso
                            cameraProvider.unbindAll()
                            try {
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    CameraSelector.DEFAULT_BACK_CAMERA,
                                    previewUseCase,
                                    analysisUseCase
                                )
                            } catch (exc: Exception) {
                                Log.e("EscanearQR", "Error al enlazar casos de uso", exc)
                            }
                        }, executor)

                        previewView
                    },
                    modifier = Modifier.fillMaxHeight(0.6f)
                )
            } else if (!permissionState.hasPermission) {
                Text("Permiso de cámara denegado.")
            }
        }
    }
}

private var ultimaLecturaExitosa: Long = 0
private var escaneoBloqueado = false

fun processImageProxy(
    imageProxy: ImageProxy,
    tipoMovimiento: String,
    viewModel: UsuarioViewModel,
    onQrDetected: (Boolean) -> Unit,
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    coroutineScope: CoroutineScope
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null && !escaneoBloqueado) { // 👈 evita procesar si ya se bloqueó
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        val scanner = BarcodeScanning.getClient()

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                if (barcodes.isNotEmpty()) {
                    val ahora = System.currentTimeMillis()
                    // 👇 Evita reacciones múltiples por frames consecutivos
                    if (ahora - ultimaLecturaExitosa < 2000) {
                        return@addOnSuccessListener
                    }
                    ultimaLecturaExitosa = ahora
                    escaneoBloqueado = true // 🔒 Bloquea el escáner completamente

                    onQrDetected(true)
                    val codigoQR = barcodes.first().rawValue ?: ""
                    val partes = codigoQR.split("|")

                    if (partes.size == 2) {
                        val usuarioId = partes[0]
                        val equipoId = partes[1]
                        val guardiaId = "5c4518e5-abc1-4bf3-b803-5fefd4d6ab9d"

                        viewModel.registrarMovimientoDesdeQR(
                            usuarioId,
                            equipoId,
                            tipoMovimiento,
                            guardiaId
                        ) { respuesta ->
                            coroutineScope.launch {
                                val mensaje = respuesta?.let {
                                    """
                                    ✅ Movimiento registrado:
                                    Nombre: ${it.nombre}
                                    Documento: ${it.documento}
                                    Carrera: ${it.carrera}
                                    Equipo: ${it.equipo}
                                    Tipo: ${it.tipoMovimiento}
                                    Fecha: ${it.fechaHora}
                                    """.trimIndent()
                                } ?: "❌ Error al registrar movimiento"
                                snackbarHostState.showSnackbar(mensaje)
                                delay(1200) // pequeño delay
                                navController.popBackStack("menu", false)
                                escaneoBloqueado = false // 🔓 libera al volver
                            }
                        }
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("⚠️ QR inválido")
                            delay(600)
                            navController.popBackStack("menu", false)
                            escaneoBloqueado = false
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