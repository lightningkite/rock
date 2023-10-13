package com.lightningkite.rock

import com.lightningkite.rock.views.ViewContext

data class GeolocationResult(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Double,
)

expect fun ViewContext.geolocate(onFixed: (GeolocationResult) -> Unit)
expect fun ViewContext.watchGeolocation(onUpdated: (GeolocationResult) -> Unit)
