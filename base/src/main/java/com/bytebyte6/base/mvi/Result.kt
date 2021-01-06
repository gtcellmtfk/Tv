package com.bytebyte6.base.mvi

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: Throwable, var handled: Boolean = false) :
        Result<Nothing>()

    object Loading : Result<Nothing>()
}

fun <T> Result<T>.success(): T? {
    return when (this) {
        is Result.Success -> data
        else -> null
    }
}

fun <T> Result<T>.isLoading(): Boolean {
    return this is Result.Loading
}

fun <T> Result<T>.isError(): Throwable? {
    return when (this) {
        is Result.Error -> {
            if (handled) {
                null
            } else {
                handled = true
                error
            }
        }
        else -> null
    }
}