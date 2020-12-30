package com.bytebyte6.base

import android.util.Log

const val GLOBAL_TAG="LOGGER: 💊💊💊 "

fun Any.logd(message: String) {
    if (BuildConfig.DEBUG) {
        Log.d(GLOBAL_TAG + javaClass.simpleName, message)
    }
}

fun Any.loge(message: String) {
    if (BuildConfig.DEBUG) {
        Log.d(GLOBAL_TAG + javaClass.simpleName, message)
    }
}