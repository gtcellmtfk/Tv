package com.bytebyte6.base.mvi

sealed class Result<out R> {
    var handled: Boolean = false

    data class Success<out T>(
        val data: T,
        /*加载更多的情况下使用表示数据全部加载完成*/
        val end: Boolean = false
    ) : Result<T>()

    data class Error(
        val error: Throwable
    ) : Result<Nothing>()

    class Loading : Result<Nothing>()
}

fun <T> Result<T>.branch(
    Success: (() -> Unit)? = null,
    Error: (() -> Unit)? = null,
    Loading: (() -> Unit)? = null
) {
    when (this) {
        is Result.Success -> {
            Success?.invoke()
        }
        is Result.Error -> {
            Error?.invoke()
        }
        is Result.Loading -> {
            Loading?.invoke()
        }
    }
}

fun <T> Result<T>.branchIfNotHandled(
    Success: (() -> Unit)? = null,
    Error: (() -> Unit)? = null,
    Loading: (() -> Unit)? = null
) {
    when (this) {
        is Result.Success -> this.doSomethingIfNotHandled { Success?.invoke() }
        is Result.Error -> this.doSomethingIfNotHandled { Error?.invoke() }
        is Result.Loading -> this.doSomethingIfNotHandled { Loading?.invoke() }
    }
}

fun <T> Result<T>.doSomethingIfNotHandled(doSomething: (() -> Unit)) {
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

fun <T> Result<T>.isLoadingAndDoSomething(doSomething: (() -> Unit)) {
    if (isLoading()) {
        doSomethingIfNotHandled {
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

fun <T> Result<T>.isErrorAndDoSomething(doSomething: (() -> Unit)) {
    if (isError() != null) {
        doSomethingIfNotHandled {
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