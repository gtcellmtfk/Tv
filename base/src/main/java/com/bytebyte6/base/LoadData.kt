package com.bytebyte6.base

interface LoadData<T> {
    fun start()
    fun success(data: T)
    fun fail(error: Throwable)
}