package com.example.proyectofinal.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

@OptIn(ExperimentalPermissionsApi::class)
val PermissionState.hasPermission: Boolean
    get() = this.status is PermissionStatus.Granted

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
fun generarCodigoQR(texto: String, size: Int = 512): Bitmap {
    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(texto, BarcodeFormat.QR_CODE, size, size)
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)

    for (x in 0 until size) {
        for (y in 0 until size) {
            bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
        }
    }

    return bitmap
}


