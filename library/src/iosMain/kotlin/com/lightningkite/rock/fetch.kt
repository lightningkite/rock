package com.lightningkite.rock

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.cinterop.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import platform.Foundation.*
import platform.darwin.NSObject
import platform.posix.memcpy
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

val client = HttpClient()

actual suspend fun fetch(
    url: String,
    method: HttpMethod,
    headers: HttpHeaders,
    body: RequestBody?
): RequestResponse {
    val response = client.request {
        this.method = when(method) {
            HttpMethod.GET -> io.ktor.http.HttpMethod.Get
            HttpMethod.POST -> io.ktor.http.HttpMethod.Post
            HttpMethod.PUT -> io.ktor.http.HttpMethod.Put
            HttpMethod.PATCH -> io.ktor.http.HttpMethod.Patch
            HttpMethod.DELETE -> io.ktor.http.HttpMethod.Delete
        }
        this.headers {
            for((key, values) in headers.map) {
                for(value in values) append(key, value)
            }
        }
        when(body) {
            is RequestBodyBlob -> {
                contentType(ContentType.parse(body.content.type))
                setBody(body.content.data.toByteArray())
            }
            is RequestBodyFile -> {
                contentType(ContentType.parse(when(body.content.url.pathExtension) {
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
                }))
                setBody(body.content.url.dataRepresentation.toByteArray())
            }
            is RequestBodyText -> {
                contentType(ContentType.parse(body.type))
                setBody(body.content)
            }
            null -> TODO()
        }
    }
    return RequestResponse(response)
}
actual inline fun httpHeaders(map: Map<String, String>): HttpHeaders = HttpHeaders(map.entries.associateTo(HashMap()) { it.key.lowercase() to listOf(it.value) })
actual inline fun httpHeaders(headers: HttpHeaders): HttpHeaders =  HttpHeaders(headers.map.toMutableMap())
actual inline fun httpHeaders(list: List<Pair<String, String>>): HttpHeaders = HttpHeaders(list.groupBy { it.first.lowercase() }.mapValues { it.value.map { it.second } }.toMutableMap())
actual class HttpHeaders(val map: MutableMap<String, List<String>>) {
    actual fun append(name: String, value: String): Unit { map[name.lowercase()] = (map[name.lowercase()] ?: listOf()) + value }
    actual fun delete(name: String): Unit { map.remove(name.lowercase()) }
    actual fun get(name: String): String? = map[name.lowercase()]?.joinToString(",")
    actual fun has(name: String): Boolean = map.containsKey(name.lowercase())
    actual fun set(name: String, value: String): Unit { map[name.lowercase()] = listOf(value) }
}
actual class RequestResponse(val wraps: HttpResponse) {
    actual val status: Short get() = wraps.status.value.toShort()
    actual val ok: Boolean get() = wraps.status.isSuccess()
    actual suspend fun text(): String = wraps.bodyAsText()
    actual suspend fun blob(): Blob = wraps.body<ByteArray>().let { Blob(it.toNSData(), wraps.contentType()?.toString() ?: "application/octet-stream") }
}

actual class Blob(val data: NSData, val type: String = "application/octet-stream")
actual class FileReference(val url: NSURL)

actual fun websocket(url: String): WebSocket {
    return WebSocketWrapper(url)
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
class WebSocketWrapper(val url: String): WebSocket {
    val closeReason = Channel<CloseReason>()
    val sending = Channel<Frame>(10)
    var stayOn = true
    init {
        launchGlobal {
            client.webSocket(url) {
                onOpen.forEach { it() }
                launch {
                    while(stayOn) {
                        send(sending.receive())
                    }
                }
                launch {
                    this@WebSocketWrapper.closeReason.receive().let { reason ->
                        close(reason)
                        onClose.forEach { it(reason.code) }
                    }
                }
                var reason: CloseReason? = null
                while(stayOn) {
                    when(val x = incoming.receive()) {
                        is Frame.Binary -> onBinaryMessage.forEach { it(Blob(x.data.toNSData())) }
                        is Frame.Text -> TODO()
                        is Frame.Close -> {
                            reason = x.readReason()
                            break
                        }
                        else -> {}
                    }
                }
                onClose.forEach { it(reason?.code ?: 0) }
            }
        }
    }
    val onOpen = ArrayList<()->Unit>()
    val onClose = ArrayList<(Short)->Unit>()
    val onMessage = ArrayList<(String)->Unit>()
    val onBinaryMessage = ArrayList<(Blob)->Unit>()
    override fun close(code: Short, reason: String) {
        stayOn = false
        launchGlobal {
            closeReason.send(CloseReason(code, reason))
            closeReason.close()
            sending.close()
        }
    }
    override fun send(data: String) {
        launchGlobal { sending.send(Frame.Text(data)) }
    }
    override fun send(data: Blob) {
        launchGlobal { sending.send(Frame.Binary(false, data.data.toByteArray())) }
    }
    override fun onOpen(action: ()->Unit) { onOpen.add(action) }
    override fun onMessage(action: (String)->Unit) { onMessage.add(action) }
    override fun onBinaryMessage(action: (Blob)->Unit) { onBinaryMessage.add(action) }
    override fun onClose(action: (Short)->Unit) { onClose.add(action) }
}

fun String.nsdata(): NSData? =
    NSString.create(string = this).dataUsingEncoding(NSUTF8StringEncoding)

fun NSData.string(): String? =
    NSString.create(data = this, encoding = NSUTF8StringEncoding)?.toString()

@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toNSData() : NSData = memScoped {
    NSData.create(bytes = allocArrayOf(this@toNSData),
        length = this@toNSData.size.toULong())
}
@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray = ByteArray(this@toByteArray.length.toInt()).apply {
    usePinned {
        memcpy(it.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
    }
}