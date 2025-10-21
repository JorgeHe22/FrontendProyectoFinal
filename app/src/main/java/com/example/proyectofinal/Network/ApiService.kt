package com.example.proyectofinal.Network

import com.example.proyectofinal.Model.DispositivoRequest
import com.example.proyectofinal.Model.EquipoRequest
import com.example.proyectofinal.Model.EquipoResponse
import com.example.proyectofinal.Model.HistorialResponse
import com.example.proyectofinal.Model.LoginRequest
import com.example.proyectofinal.Model.MovimientoQRRequest
import com.example.proyectofinal.Model.RegistroMovimientoResponse
import com.example.proyectofinal.Model.UsuarioRequest
import com.example.proyectofinal.Model.UsuarioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("api/usuarios")
    suspend fun registrarUsuario(@Body usuario: UsuarioRequest): Response<Void>

    @POST("api/login")
    suspend fun loginEstudiante(@Body request: LoginRequest): Response<UsuarioResponse>

    @GET("api/equipos/usuario/{usuarioId}")
    suspend fun obtenerEquiposPorUsuario(@Path("usuarioId") usuarioId: String): Response<List<DispositivoRequest>>

    @POST("api/equipos")
    suspend fun registrarDispositivo(@Body request: DispositivoRequest): Response<Void>

    @POST("api/movimientos/escanear-qr")
    suspend fun registrarMovimientoDesdeQR(
        @Body request: MovimientoQRRequest
    ): Response<RegistroMovimientoResponse>

    @GET("api/movimientos/usuario/{usuarioId}")
    suspend fun getHistorial(
        @Path("usuarioId") usuarioId: String
    ): Response<List<HistorialResponse>>

    @GET("historial/{usuarioId}/tipo/{tipo}")
    suspend fun getHistorialPorTipo(
        @Path("usuarioId") usuarioId: String,
        @Path("tipo") tipo: String
    ): Response<List<HistorialResponse>>

    @PUT("/api/equipos/{id}")
    suspend fun actualizarEquipo(
        @Path("id") id: String,
        @Body equipo: DispositivoRequest
    ): Response<DispositivoRequest>


}