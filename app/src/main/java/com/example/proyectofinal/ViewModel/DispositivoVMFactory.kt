package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.Network.ApiService

class DispositivoVMFactory(
    private val apiService: ApiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DispositivoViewModel::class.java)) {
            return DispositivoViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}