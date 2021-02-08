package com.bytebyte6.common.test

fun assertError(block: () -> Unit) {
    try {
        block()
        assert(false)
    } catch (e: Exception) {
        assert(true)
    }
}