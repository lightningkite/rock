package com.lightningkite.mppexample


expect class ActivityIndicator : NView
typealias LoadingSpinner = ActivityIndicator

expect fun ViewContext.nativeActivityIndicator(setup: ActivityIndicator.() -> Unit = {}): Unit
expect var ActivityIndicator.color: Color
expect var ActivityIndicator.width: Dimension
expect var ActivityIndicator.height: Dimension
expect var ActivityIndicator.lineWidth: Dimension
