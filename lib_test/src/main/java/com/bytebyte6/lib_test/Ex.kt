package com.bytebyte6.lib_test

import android.annotation.SuppressLint

/**
 * 断言block抛出异常
 */
@SuppressLint("Assert")
fun assertError(block: () -> Unit) {
    try {
        block()
        assert(false)
    } catch (e: Exception) {
        assert(true)
    }
}