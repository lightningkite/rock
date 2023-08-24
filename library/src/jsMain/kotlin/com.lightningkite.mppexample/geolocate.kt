package com.lightningkite.mppexample

import kotlinx.browser.window

actual fun ViewContext.geolocate(onFixed: (Double, Double) -> Unit) {
    window.navigator.asDynamic().geolocation.getCurrentPosition(
        { position: GeolocationPosition -> onFixed(position.coords.latitude, position.coords.longitude) }
    )
}

external abstract class GeolocationPosition {
    val coords: GeolocationCoordinates
}
external abstract class GeolocationCoordinates {
    val latitude: Double
    val longitude: Double
}
