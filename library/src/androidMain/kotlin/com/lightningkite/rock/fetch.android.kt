@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.lightningkite.rock

import android.net.Uri
import com.lightningkite.rock.views.AndroidAppContext
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import kotlin.coroutines.CoroutineContext

val httpClient: HttpClient get() {
    return AndroidAppContext.ktorClient
}
actual suspend fun fetch(
    url: String,
    method: HttpMethod,
    headers: HttpHeaders,
    body: RequestBody?
): RequestResponse {
    val response = httpClient.request {
        this.method = when(method) {
            HttpMethod.GET -> io.ktor.http.HttpMethod.Get
            HttpMethod.POST -> io.ktor.http.HttpMethod.Post
            HttpMethod.PUT -> io.ktor.http.HttpMethod.Put
            HttpMethod.PATCH -> io.ktor.http.HttpMethod.Patch
            HttpMethod.DELETE -> io.ktor.http.HttpMethod.Delete
        }
        this.headers {
            headers.map.entries.forEach { entry ->
                append(entry.key, entry.value)
            }
        }
        when(body) {
            is RequestBodyBlob -> {
                contentType(ContentType.parse("application/octet-stream"))
                setBody(body.content)
            }
            is RequestBodyFile -> {
                contentTypeFromExtension(body)
                setBody(body.content)
            }
            is RequestBodyText -> {
                contentType(ContentType.parse(body.content))
                setBody(body.content)
            }
            null -> {
                Timber.d("No Body for fetch request $url $method")
            }
        }
    }
    return RequestResponse(response)
}
actual class HttpHeaders {
    private val mutableMap = mutableMapOf<String, String>()
    val map: Map<String, String> get() = mutableMap
    actual fun append(name: String, value: String) {
        mutableMap[name] = value
    }

    actual fun delete(name: String) {
        mutableMap.remove(name)
    }

    actual fun get(name: String): String? {
        return mutableMap[name]
    }

    actual fun has(name: String): Boolean {
        return mutableMap[name] != null
    }

    actual fun set(name: String, value: String) {
        mutableMap[name] = value
    }
}

actual class RequestResponse(private val response: HttpResponse) {
    actual val status: Short
        get() = response.status.value.toShort()
    actual val ok: Boolean
        get() = response.status.value in 200..299

    actual suspend fun text(): String {
        return response.body()
    }

    actual suspend fun blob(): Blob {
        return response.body()
    }
}

actual typealias FileReference = File
actual typealias Blob = ByteArray
val webSocketClient: HttpClient by lazy {
    HttpClient(CIO) {
        install(WebSockets) {
            pingInterval = 20_000
        }
    }
}

actual fun websocket(url: String): WebSocket {
    val uri = Uri.parse(url)

    return object : WebSocket, CoroutineScope {
        private var onOpenReceiver: (() -> Unit)? = null
        private var onMessageReceiver: ((String) -> Unit)? = null
        private var onBinaryMessageReceiver: ((Blob) -> Unit)? = null
        private var onCloseReceiver: ((Short) -> Unit)? = null
        private var socketSession: DefaultClientWebSocketSession? = null
        override val coroutineContext: CoroutineContext = Job()
        init {
            launch {
                webSocketClient.webSocket(method = io.ktor.http.HttpMethod.Get, host = uri.host, port = uri.port, path = uri.path) {
                    socketSession = this
                    onOpenReceiver?.invoke()
                    while(true) {
                        val othersMessage = incoming.receive() as? Frame.Text
                        Timber.d("GOT WS MESSAGE: $othersMessage")
                    }
                }
            }
        }

        override fun close(code: Short, reason: String) {
            launch {
                socketSession?.close(CloseReason(code, reason))
            }

            Timber.d("")
            onCloseReceiver?.invoke(code)
            socketSession = null
        }

        override fun send(data: String) {
            if (socketSession == null) {
                throw RuntimeException("No socket session, do not call send before receiving" +
                " onOpen or after receiving onClose")
            }
            launch { socketSession?.send(data) }
        }

        override fun send(data: Blob) {
            if (socketSession == null) {
                throw RuntimeException("No socket session, do not call send before receiving" +
                " onOpen or after receiving onClose")
            }
            launch { socketSession?.send(data) }
        }

        override fun onOpen(action: () -> Unit) {
            this.onOpenReceiver = action
            if (this.socketSession != null) {
                this.onOpenReceiver?.invoke()
            }
        }

        override fun onMessage(action: (String) -> Unit) {
            this.onMessageReceiver = action
        }

        override fun onBinaryMessage(action: (Blob) -> Unit) {
            this.onBinaryMessageReceiver = action
        }

        override fun onClose(action: (Short) -> Unit) {
            this.onCloseReceiver = action
        }
    }
}

actual inline fun httpHeaders(map: Map<String, String>): HttpHeaders {
    return map.entries.fold(HttpHeaders()) { headers, entry ->
        headers.append(
            entry.key,
            entry.value
        )
        headers
    }
}

actual inline fun httpHeaders(list: List<Pair<String, String>>): HttpHeaders {
    return list.fold(HttpHeaders()) { headers, pair ->
        headers.append(pair.first, pair.second)
        headers
    }
}

actual inline fun httpHeaders(headers: HttpHeaders): HttpHeaders {
    return headers.map.entries.fold(HttpHeaders()) { headerCopy, entry ->
        headerCopy.append(entry.key, entry.value)
        headerCopy
    }
}

private fun HttpRequestBuilder.contentTypeFromExtension(body: RequestBodyFile) {
    contentType(
        ContentType.parse(
            when (body.content.extension) {
                "html" -> "text/html"
                "htm" -> "text/html"
                "shtml" -> "text/html"
                "css" -> "text/css"
                "xml" -> "text/xml"
                "gif" -> "image/gif"
                "jpeg" -> "image/jpeg"
                "jpg" -> "image/jpeg"
                "js" -> "application/javascript"
                "atom" -> "application/atom+xml"
                "rss" -> "application/rss+xml"
                "mml" -> "text/mathml"
                "txt" -> "text/plain"
                "jad" -> "text/vnd.sun.j2me.app-descriptor"
                "wml" -> "text/vnd.wap.wml"
                "htc" -> "text/x-component"
                "png" -> "image/png"
                "tif" -> "image/tiff"
                "tiff" -> "image/tiff"
                "wbmp" -> "image/vnd.wap.wbmp"
                "ico" -> "image/x-icon"
                "jng" -> "image/x-jng"
                "bmp" -> "image/x-ms-bmp"
                "svg" -> "image/svg+xml"
                "svgz" -> "image/svg+xml"
                "webp" -> "image/webp"
                "woff" -> "application/font-woff"
                "jar" -> "application/java-archive"
                "war" -> "application/java-archive"
                "ear" -> "application/java-archive"
                "json" -> "application/json"
                "hqx" -> "application/mac-binhex40"
                "doc" -> "application/msword"
                "pdf" -> "application/pdf"
                "ps" -> "application/postscript"
                "eps" -> "application/postscript"
                "ai" -> "application/postscript"
                "rtf" -> "application/rtf"
                "m3u8" -> "application/vnd.apple.mpegurl"
                "xls" -> "application/vnd.ms-excel"
                "eot" -> "application/vnd.ms-fontobject"
                "ppt" -> "application/vnd.ms-powerpoint"
                "wmlc" -> "application/vnd.wap.wmlc"
                "kml" -> "application/vnd.google-earth.kml+xml"
                "kmz" -> "application/vnd.google-earth.kmz"
                "7z" -> "application/x-7z-compressed"
                "cco" -> "application/x-cocoa"
                "jardiff" -> "application/x-java-archive-diff"
                "jnlp" -> "application/x-java-jnlp-file"
                "run" -> "application/x-makeself"
                "pl" -> "application/x-perl"
                "pm" -> "application/x-perl"
                "prc" -> "application/x-pilot"
                "pdb" -> "application/x-pilot"
                "rar" -> "application/x-rar-compressed"
                "rpm" -> "application/x-redhat-package-manager"
                "sea" -> "application/x-sea"
                "swf" -> "application/x-shockwave-flash"
                "sit" -> "application/x-stuffit"
                "tcl" -> "application/x-tcl"
                "tk" -> "application/x-tcl"
                "der" -> "application/x-x509-ca-cert"
                "pem" -> "application/x-x509-ca-cert"
                "crt" -> "application/x-x509-ca-cert"
                "xpi" -> "application/x-xpinstall"
                "xhtml" -> "application/xhtml+xml"
                "xspf" -> "application/xspf+xml"
                "zip" -> "application/zip"
                "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation"
                "mid" -> "audio/midi"
                "midi" -> "audio/midi"
                "kar" -> "audio/midi"
                "mp3" -> "audio/mpeg"
                "ogg" -> "audio/ogg"
                "m4a" -> "audio/x-m4a"
                "ra" -> "audio/x-realaudio"
                "3gpp" -> "video/3gpp"
                "3gp" -> "video/3gpp"
                "ts" -> "video/mp2t"
                "mp4" -> "video/mp4"
                "mpeg" -> "video/mpeg"
                "mpg" -> "video/mpeg"
                "mov" -> "video/quicktime"
                "webm" -> "video/webm"
                "flv" -> "video/x-flv"
                "m4v" -> "video/x-m4v"
                "mng" -> "video/x-mng"
                "asx" -> "video/x-ms-asf"
                "asf" -> "video/x-ms-asf"
                "wmv" -> "video/x-ms-wmv"
                "avi" -> "video/x-msvideo"
                else -> "application/octet-stream"
            }
        )
    )
}