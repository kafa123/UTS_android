package com.example.uts_android.service

import java.io.FileOutputStream
import java.net.URL

class ImageService {
    fun downloadImage(url: String, filePath: String) {
        val connection = URL(url).openConnection()
        val inputStream = connection.getInputStream()
        val fileOutputStream = FileOutputStream(filePath)

        inputStream.use { input ->
            fileOutputStream.use { output ->
                input.copyTo(output)
            }
        }
    }
}