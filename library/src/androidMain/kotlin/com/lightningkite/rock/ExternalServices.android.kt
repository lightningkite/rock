package com.lightningkite.rock

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.lightningkite.rock.views.AndroidAppContext
import java.io.File

actual object ExternalServices {
    actual fun openTab(url: String) {
        AndroidAppContext.applicationCtx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    actual fun requestFile(
        mimeTypes: List<String>,
        onResult: (FileReference?) -> Unit,
    ) {
        AndroidAppContext.requestPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE) {
            if (!it.accepted) return@requestPermissions onResult(null)

            val type = mimeTypes.joinToString(",")

            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = type

            val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = type

            val chooserIntent = Intent.createChooser(getIntent, "Select items")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

            AndroidAppContext.startActivityForResult(chooserIntent) { code, data ->
                if (code == Activity.RESULT_OK) {
                    onResult(data?.data?.let(::FileReference))
                } else {
                    onResult(null)
                }
            }
        }
    }

    actual fun requestFiles(
        mimeTypes: List<String>,
        onResult: (List<FileReference>) -> Unit,
    ) {

        val permsHandler: (RockActivity.PermissionResult) -> Unit = {
            if (!it.accepted) onResult(listOf())

            val type = mimeTypes.joinToString(",")

            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = type
            getIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

            val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = type

            val chooserIntent = Intent.createChooser(getIntent, "Select items")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

            AndroidAppContext.startActivityForResult(chooserIntent) { code, data ->
                if (code == Activity.RESULT_OK) {
                    onResult(
                        data?.clipData?.let {
                            (0 until it.itemCount).map { index ->
                                it.getItemAt(index).uri.let(::FileReference)
                            }
                        }
                            ?: data?.data?.let(::FileReference)?.let(::listOf)
                            ?: listOf()
                    )
                } else {
                    onResult(listOf())
                }
            }
        }

        if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            val permissions = buildList {
                if (mimeTypes.any { it.startsWith("image") }) add(android.Manifest.permission.READ_MEDIA_IMAGES)
                if (mimeTypes.any { it.startsWith("video") }) add(android.Manifest.permission.READ_MEDIA_VIDEO)
                if (mimeTypes.any { it.startsWith("audio") }) add(android.Manifest.permission.READ_MEDIA_AUDIO)
            }.toTypedArray()
            AndroidAppContext.requestPermissions(*permissions, onResult = permsHandler)
        } else {
            AndroidAppContext.requestPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                onResult = permsHandler
            )
        }
    }

    actual fun requestCaptureSelf(
        mimeTypes: List<String>,
        onResult: (FileReference?) -> Unit,
    ) {
        if (mimeTypes.all { it.startsWith("image/") }) requestImageCamera(
            true,
            MediaStore.ACTION_IMAGE_CAPTURE,
            onResult
        )
        else if (mimeTypes.all { it.startsWith("video/") }) requestImageCamera(
            true,
            MediaStore.ACTION_VIDEO_CAPTURE,
            onResult
        )
        else System.err.println("Captures besides images and video not supported yet. Requested $mimeTypes")
    }

    actual fun requestCaptureEnvironment(
        mimeTypes: List<String>,
        onResult: (FileReference?) -> Unit,
    ) {
        if (mimeTypes.all { it.startsWith("image/") }) requestImageCamera(
            false,
            MediaStore.ACTION_IMAGE_CAPTURE,
            onResult
        )
        else if (mimeTypes.all { it.startsWith("video/") }) requestImageCamera(
            false,
            MediaStore.ACTION_VIDEO_CAPTURE,
            onResult
        )
        else System.err.println("Captures besides images and video not supported yet. Requested $mimeTypes")
    }

    private fun requestImageCamera(
        front: Boolean = false,
        capture: String = MediaStore.ACTION_IMAGE_CAPTURE,
        onResult: (FileReference?) -> Unit,
    ) {
        val fileProviderAuthority = AndroidAppContext.applicationCtx.packageName + ".fileprovider"
        val file = File(AndroidAppContext.applicationCtx.cacheDir, "images").also { it.mkdirs() }
            .let { File.createTempFile("image", ".jpg", it) }
            .let { FileProvider.getUriForFile(AndroidAppContext.applicationCtx, fileProviderAuthority, it) }

        AndroidAppContext.requestPermissions(android.Manifest.permission.CAMERA) {
            if (!it.accepted) return@requestPermissions onResult(null)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, file)
            if (front) {
                intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1)
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1)
                intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
            }
            AndroidAppContext.startActivityForResult(intent) { code, data ->
                if (code == Activity.RESULT_OK) {
                    onResult(data?.data?.let(::FileReference))
                } else {
                    onResult(null)
                }
            }
        }
    }
    actual fun setClipboardText(value: String) {
        (AndroidAppContext.activityCtx?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(ClipData.newPlainText(value, value))
    }
}