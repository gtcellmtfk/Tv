package com.bytebyte6.base.mvi

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

fun <T> Result<T>.success(): T? {
    return when (this) {
        is Result.Success -> data
        else -> null
    }
}