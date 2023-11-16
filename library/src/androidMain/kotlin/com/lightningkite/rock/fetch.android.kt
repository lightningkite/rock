package com.lightningkite.rock

actual suspend fun fetch(
    url: String,
    method: HttpMethod,
    headers: HttpHeaders,
    body: RequestBody?,
): RequestResponse {
    TODO("Not yet implemented")
}

actual inline fun httpHeaders(map: Map<String, String>): HttpHeaders {
    TODO("Not yet implemented")
}

actual inline fun httpHeaders(list: List<Pair<String, String>>): HttpHeaders {
    TODO("Not yet implemented")
}

actual inline fun httpHeaders(headers: HttpHeaders): HttpHeaders {
    TODO("Not yet implemented")
}

actual class HttpHeaders {
    actual fun append(name: String, value: String) {
    }

    actual fun delete(name: String) {
    }

    actual fun get(name: String): String? {
        TODO("Not yet implemented")
    }

    actual fun has(name: String): Boolean {
        TODO("Not yet implemented")
    }

    actual fun set(name: String, value: String) {
    }
}

actual class RequestResponse {
    actual val status: Short
        get() = TODO("Not yet implemented")
    actual val ok: Boolean
        get() = TODO("Not yet implemented")

    actual suspend fun text(): String {
        TODO("Not yet implemented")
    }

    actual suspend fun blob(): Blob {
        TODO("Not yet implemented")
    }
}

actual class Blob
actual class FileReference

actual fun websocket(url: String): WebSocket {
    TODO("Not yet implemented")
}