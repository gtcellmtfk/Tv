package com.bytebyte6.base

import android.util.Log

const val GLOBAL_TAG = "LOGGER: 💊💊💊 "

fun Any.logd(message: String, tag: String? = null) {
    if (BuildConfig.DEBUG) {
        Log.d(GLOBAL_TAG + (tag ?: javaClass.simpleName), message)
    }
}

fun Any.loge(message: String, tag: String? = null) {
    if (BuildConfig.DEBUG) {
        Log.d(GLOBAL_TAG + (tag ?: javaClass.simpleName), message)
    }
}