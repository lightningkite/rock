package com.lightningkite.kiteui

import com.lightningkite.kiteui.GeolocationPosition
import com.lightningkite.kiteui.views.ViewWriter
import kotlinx.browser.window

external interface AltNav: JsAny {
    val geolocation: Geolocation
}
external interface Geolocation: JsAny{
    fun getCurrentPosition(result: (GeolocationPosition) -> Unit)
    fun watchPosition(result: (GeolocationPosition) -> Unit)
}

actual fun ViewWriter.geolocate(onFixed: (GeolocationResult) -> Unit) {
    window.navigator.unsafeCast<AltNav>().geolocation.getCurrentPosition { position: GeolocationPosition ->
        onFixed(
            GeolocationResult(
                latitude = position.coords.latitude,
                longitude = position.coords.longitude,
                accuracy = position.coords.accuracy,
            )
        )
    }
}

actual fun ViewWriter.watchGeolocation(onUpdated: (GeolocationResult) -> Unit) {
    window.navigator.unsafeCast<AltNav>().geolocation.watchPosition { position: GeolocationPosition ->
        onUpdated(
            GeolocationResult(
                latitude = position.coords.latitude,
                longitude = position.coords.longitude,
                accuracy = position.coords.accuracy,
            )
        )
    }
}

abstract external class GeolocationPosition {
    val coords: GeolocationCoordinates
}
abstract external class GeolocationCoordinates {
    val latitude: Double
    val longitude: Double
    val accuracy: Double
}
