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

    override fun toString(): String {
        return "Result(handled=$handled)"
    }
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
        is Result.Error -> {
            error?.invoke(this)
        }
        is Result.Loading -> {
            loading?.invoke(this)
        }
    }
}

fun <T> Result<T>.emitIfNotHandled(
    success: ((s: Result.Success<T>) -> Unit)? = null,
    error: ((e: Result.Error) -> Unit)? = null,
    loading: ((l: Result.Loading) -> Unit)? = null
) {
    when (this) {
        is Result.Success -> this.doSomethingIfNotHandled { success?.invoke(this) }
        is Result.Error -> this.doSomethingIfNotHandled { error?.invoke(this) }
        is Result.Loading -> this.doSomethingIfNotHandled { loading?.invoke(this) }
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