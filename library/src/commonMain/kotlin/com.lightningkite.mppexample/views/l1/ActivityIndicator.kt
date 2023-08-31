package com.lightningkite.mppexample


expect class ActivityIndicator : NView
typealias LoadingSpinner = ActivityIndicator

expect fun ViewContext.activityIndicator(setup: ActivityIndicator.() -> Unit = {}): Unit
expect var ActivityIndicator.color: Color
