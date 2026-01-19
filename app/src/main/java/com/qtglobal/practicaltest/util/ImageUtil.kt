package com.qtglobal.practicaltest.util

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap


object ImageUtil {

    // Converts a ByteArray to an ImageBitmap for Compose UI.
    fun byteArrayToImageBitmap(byteArray: ByteArray): ImageBitmap? {
        return try {
            if (byteArray.isEmpty()) {
                return null
            }

            val bitmap = BitmapFactory.decodeByteArray(
                byteArray,
                0,
                byteArray.size
            )

            bitmap?.asImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}