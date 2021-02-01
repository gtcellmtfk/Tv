package com.bytebyte6.common

import android.util.Log

const val GLOBAL_TAG = "LOGGER: 💊💊💊 "

object LogEx {
    var logger: Boolean = false
}

fun Any.logd(message: String, tag: String? = null) {
    if (LogEx.logger) {
        Log.d(GLOBAL_TAG + (tag ?: javaClass.simpleName), message)
    }
}

fun Any.loge(message: String, tag: String? = null) {
    if (LogEx.logger) {
        Log.e(GLOBAL_TAG + (tag ?: javaClass.simpleName), message)
    }
}