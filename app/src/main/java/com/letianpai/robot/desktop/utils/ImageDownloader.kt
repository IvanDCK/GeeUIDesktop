package com.letianpai.robot.desktop.utils

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Request.Builder
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageDownloader {
    fun downloadImage(urlString: String, destinationPath: String): String? {
        val client = OkHttpClient()
        val request: Request = Builder()
            .url(urlString)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful && response.body != null) {
                    val inputStream = response.body!!.byteStream()
                    val destinationFile = File(destinationPath)
                    val outputStream = FileOutputStream(destinationFile)

                    val buffer = ByteArray(1024)
                    var bytesRead: Int

                    while ((inputStream.read(buffer).also { bytesRead = it }) != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }

                    outputStream.close()
                    inputStream.close()

                    return destinationPath
                } else {
                    return null
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}
