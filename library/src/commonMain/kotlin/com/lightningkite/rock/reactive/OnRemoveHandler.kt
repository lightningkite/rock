package com.lightningkite.rock.reactive

interface OnRemoveHandler {
    fun onRemove(action: () -> Unit)
    fun onTry() {}
    fun onOk() {}
    fun onFail() {}
    fun onLoading() {}
}