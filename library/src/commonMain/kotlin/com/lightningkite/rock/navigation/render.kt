package com.lightningkite.rock.navigation

import com.lightningkite.rock.encodeURIComponent

fun UrlLikePath.render() = "/" + segments.joinToString("/") + (parameters.takeUnless { it.isEmpty() }?.entries?.joinToString("&", "?") { "${it.key}=${encodeURIComponent(it.value)}" } ?: "")
