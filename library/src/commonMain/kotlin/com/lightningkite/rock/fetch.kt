package com.lightningkite.rock

suspend inline fun fetch(
    url: String,
    method: HttpMethod = HttpMethod.GET,
    headers: HttpHeaders = httpHeaders(),
    body: String
) = fetch(url = url, method = method, headers = headers, body = RequestBodyText(body))
suspend inline fun fetch(
    url: String,
    method: HttpMethod = HttpMethod.GET,
    headers: HttpHeaders = httpHeaders(),
    body: Blob
) = fetch(url = url, method = method, headers = headers, body = RequestBodyBlob(body))
suspend inline fun fetch(
    url: String,
    method: HttpMethod = HttpMethod.GET,
    headers: HttpHeaders = httpHeaders(),
    body: FileReference
) = fetch(url = url, method = method, headers = headers, body = RequestBodyFile(body))
expect suspend fun fetch(
    url: String,
    method: HttpMethod = HttpMethod.GET,
    headers: HttpHeaders = httpHeaders(),
    body: RequestBody? = null
): RequestResponse

enum class HttpMethod { GET, POST, PUT, PATCH, DELETE }

expect inline fun httpHeaders(map: Map<String, String> = mapOf()): HttpHeaders
expect inline fun httpHeaders(list: List<Pair<String, String>>): HttpHeaders
expect inline fun httpHeaders(headers: HttpHeaders): HttpHeaders
expect class HttpHeaders {
    fun append(name: String, value: String)
    fun delete(name: String)
    fun get(name: String): String?
    fun has(name: String): Boolean
    fun set(name: String, value: String)
}

expect class RequestResponse {
    val status: Short
    val ok: Boolean
    suspend fun text(): String
    suspend fun blob(): Blob
}

expect class Blob
expect class FileReference

sealed interface RequestBody
data class RequestBodyText(val content: String): RequestBody
data class RequestBodyBlob(val content: Blob): RequestBody
data class RequestBodyFile(val content: FileReference): RequestBody
