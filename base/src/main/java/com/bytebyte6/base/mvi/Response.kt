package com.bytebyte6.base.mvi

sealed class Response<T> {
    data class Success<T>(val data: T) : Response<T>()
    data class Error(val error: Throwable) : Response<Throwable>()
    object Loading : Response<Nothing>()
}