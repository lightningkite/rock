package com.lightningkite.rock.views

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import com.lightningkite.rock.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.HttpURLConnection
import java.net.URL

object LoadRemoteImageScope : CoroutineScope {
    override val coroutineContext = Job()
    fun bitmapFromUrl(url: String, onBitmapLoaded: (Bitmap) -> Unit, onError: (Exception) -> Unit) {
        launch {
            val androidURL = URL(url)
            val connection = androidURL.openConnection() as HttpURLConnection
            try {
                connection.inputStream.use { inputStream ->
                    val bytes = inputStream.readBytes()
                    val bitmap: Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    onBitmapLoaded(bitmap)
                }
            } catch(ex: Exception) {
                onError(ex)
            }
        }
    }
}