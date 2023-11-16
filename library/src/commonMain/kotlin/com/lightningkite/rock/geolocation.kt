package com.lightningkite.rock

import com.lightningkite.rock.views.ViewWriter

data class GeolocationResult(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Double,
)

expect fun ViewWriter.geolocate(onFixed: (GeolocationResult) -> Unit)
expect fun ViewWriter.watchGeolocation(onUpdated: (GeolocationResult) -> Unit)
