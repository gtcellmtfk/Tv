package com.bytebyte6.base.mvi

/**
 * 添加到代码模板方便书写
 * when (result) {
 *      is Result.Success -> {
 *
 *      }
 *      is Result.Error -> {
 *
 *      }
 *      Result.Loading -> {
 *
 *      }
 * }
 */

sealed class Result<out R> {
    data class Success<out T>(val data: T, /*加载更多的情况下使用表示数据全部加载完成*/val end: Boolean = false) : Result<T>()
    data class Error(val error: Throwable) :
        Result<Nothing>()

    object Loading : Result<Nothing>()
}

fun <T> Result<T>.isSuccess(): T? {
    return when (this) {
        is Result.Success -> data
        else -> null
    }
}

fun <T> Result<T>.isLoading(): Boolean {
    return when (this) {
        is Result.Loading -> true
        else -> false
    }
}

fun <T> Result<T>.isError(): Throwable? {
    return when (this) {
        is Result.Error -> error
        else -> null
    }
}