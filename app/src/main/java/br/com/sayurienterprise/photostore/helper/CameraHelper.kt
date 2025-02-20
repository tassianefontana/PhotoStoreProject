package br.com.sayurienterprise.photostore.helper

import android.content.Context
import android.graphics.ImageFormat
import android.media.ImageReader
import android.util.DisplayMetrics
import android.view.WindowManager

class CameraHelper (private val context: Context){

    fun setupImageReader(): ImageReader {
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        // 80% of screen
        val textureViewHeight = (screenHeight * 0.8).toInt()

        // Configure ImageReader and return
        return ImageReader.newInstance(
            screenWidth,
            textureViewHeight,
            ImageFormat.JPEG,
            2
        )
    }
}