package com.example.proyectofinal.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://proyectofinal-p8ao.onrender.com/"//"http://127.0.0.1/"  // url de prueba, cambiar mas adelante a 127.0.0.1       10.0.2.2:8081

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}