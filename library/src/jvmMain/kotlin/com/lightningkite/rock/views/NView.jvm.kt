package com.lightningkite.rock.views

actual class NContext { companion object { val shared = NContext() }}
actual val NView.nContext: NContext get() = NContext.shared
actual fun NView.removeNView(child: NView) {
}

actual fun NView.listNViews(): List<NView> = listOf()