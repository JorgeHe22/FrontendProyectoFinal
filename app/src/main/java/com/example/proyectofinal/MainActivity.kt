package com.example.proyectofinal


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectofinal.Screen.AccionesEstudiante
import com.example.proyectofinal.Screen.ActualizarEquipoScreen
import com.example.proyectofinal.Screen.EscanearQRScreen
import com.example.proyectofinal.Screen.EscogerMovimientos
import com.example.proyectofinal.Screen.HistorialScreen
import com.example.proyectofinal.Screen.LoginEstudiantes
import com.example.proyectofinal.Screen.LoginGuardia
import com.example.proyectofinal.Screen.PantallaRegistroUsuario
import com.example.proyectofinal.Screen.PerfilUsuario
import com.example.proyectofinal.Screen.RegistroDispositivo
import com.example.proyectofinal.Screen.ScreenA
import com.example.proyectofinal.Screen.SeleccionLoginScreen
import com.example.proyectofinal.ViewModel.HistorialViewModel
import com.example.proyectofinal.ViewModel.UsuarioViewModel
import com.example.proyectofinal.ui.theme.ProyectoFinalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProyectoFinalTheme {
                val navController = rememberNavController()

                val usuarioViewModel: UsuarioViewModel = viewModel()

                NavHost(navController = navController, startDestination = "menu") {
                    composable("menu") {
                        ScreenA(navController, usuarioViewModel)
                    }
                    composable("registroUsuario") {
                        PantallaRegistroUsuario(
                            navController = navController,
                            viewModel = usuarioViewModel,
                            onRegistroExitoso = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("seleccionLogin") {
                        SeleccionLoginScreen(navController)
                    }
                    composable("loginEstudiante") {
                        LoginEstudiantes(navController, usuarioViewModel)
                    }
                    composable("loginGuardia") {
                        LoginGuardia(navController, usuarioViewModel)
                    }
                    composable("perfilUsuario") {
                        PerfilUsuario(navController, usuarioViewModel)
                    }
                    composable("accionesEstudiante") {
                        AccionesEstudiante(navController, usuarioViewModel)
                    }
                    composable("registroDispositivo") {
                        RegistroDispositivo(navController, usuarioViewModel)
                    }
                    composable("escogerMovimiento") {
                        EscogerMovimientos(navController, usuarioViewModel)
                    }
                    composable(
                        route = "escanearQR/{tipo}",
                        arguments = listOf(navArgument("tipo") { defaultValue = "entrada" })
                    ) { backStackEntry ->
                        val tipo = backStackEntry.arguments?.getString("tipo") ?: "entrada"
                        EscanearQRScreen(navController, tipo)
                    }
                    composable(
                        route = "historial/{usuarioId}",
                        arguments = listOf(navArgument("usuarioId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val usuarioId = backStackEntry.arguments?.getString("usuarioId") ?: ""

                        val factory = remember {
                            com.example.proyectofinal.ViewModel.HistorialVMFactory(
                                com.example.proyectofinal.Network.RetrofitClient.apiService
                            )
                        }

                        val historialViewModel: com.example.proyectofinal.ViewModel.HistorialViewModel =
                            viewModel(factory = factory)

                        // ✅ Cargar historial solo cuando haya usuarioId
                        LaunchedEffect(usuarioId) {
                            if (usuarioId.isNotEmpty()) {
                                historialViewModel.cargarHistorial(usuarioId)
                            }
                        }

                        // ✅ Llama la pantalla sin usuarioId, solo el ViewModel y navController
                        com.example.proyectofinal.Screen.HistorialScreen(
                            viewModel = historialViewModel,
                            navController = navController,
                            usuarioId = usuarioId
                        )
                    }
                    composable("actualizarEquipo") {
                        ActualizarEquipoScreen(
                            navController = navController,
                            dispositivoViewModel = viewModel()
                        )
                    }


                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProyectoFinalTheme {
        Greeting("Android")
    }
}