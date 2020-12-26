package com.bytebyte6.data

interface LoadData<T> {
    fun start()
    fun success(data: T)
    fun fail(error: Throwable)
}