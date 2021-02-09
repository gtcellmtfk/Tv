package com.bytebyte6.lib_test

fun assertError(block: () -> Unit) {
    try {
        block()
        assert(false)
    } catch (e: Exception) {
        assert(true)
    }
}