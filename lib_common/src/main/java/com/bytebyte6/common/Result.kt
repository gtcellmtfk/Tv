package com.bytebyte6.common

import androidx.lifecycle.LiveData

sealed class Result<out R> {
    var handled: Boolean = false

    data class Success<out T>(
        val data: T,
        /**加载更多的情况下使用表示数据全部加载完成*/
        val end: Boolean = false
    ) : Result<T>()

    data class Error(
        val error: Throwable
    ) : Result<Nothing>()

    class Loading : Result<Nothing>()
}

fun <T> Result<T>.emit(
    success: ((s: Result.Success<T>) -> Unit)? = null,
    error: ((e: Result.Error) -> Unit)? = null,
    loading: ((l: Result.Loading) -> Unit)? = null
) {
    when (this) {
        is Result.Success -> {
            success?.invoke(this)
        }
        is Result.Error -> this.runIfNotHandled { error?.invoke(this) }
        is Result.Loading -> this.runIfNotHandled { loading?.invoke(this) }
    }
}

fun <T> Result<T>.emitIfNotHandled(
    success: ((s: Result.Success<T>) -> Unit)? = null,
    error: ((e: Result.Error) -> Unit)? = null,
    loading: ((l: Result.Loading) -> Unit)? = null
) {
    when (this) {
        is Result.Success -> this.runIfNotHandled { success?.invoke(this) }
        is Result.Error -> this.runIfNotHandled { error?.invoke(this) }
        is Result.Loading -> this.runIfNotHandled { loading?.invoke(this) }
    }
}

fun <T> Result<T>.runIfNotHandled(doSomething: (() -> Unit)) {
    if (!handled) {
        handled = true
        doSomething()
    }
}

fun <T> Result<T>.isSuccess(): T? {
    return when (this) {
        is Result.Success -> data
        else -> null
    }
}

fun <T> Result<T>.isLoadingAndRun(doSomething: (() -> Unit)) {
    if (isLoading()) {
        runIfNotHandled {
            doSomething.invoke()
        }
    }
}

fun <T> Result<T>.isLoading(): Boolean {
    return when (this) {
        is Result.Loading -> true
        else -> false
    }
}

fun <T> Result<T>.isErrorAndRun(doSomething: (() -> Unit)) {
    if (isError() != null) {
        runIfNotHandled {
            doSomething.invoke()
        }
    }
}

fun <T> Result<T>.isError(): Throwable? {
    return when (this) {
        is Result.Error -> error
        else -> null
    }
}

fun <T> LiveData<Result<T>>.getSuccessData() = this.value?.isSuccess()
fun <T> LiveData<Result<T>>.getError() = this.value?.isError()