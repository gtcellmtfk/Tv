package com.bytebyte6.base

import androidx.lifecycle.Observer

abstract class ResultObserver<T> : Observer<Result<T>> {
    override fun onChanged(result: Result<T>?) {
        when (result) {
            is Result.Success -> {
                success(result.data, result.end)
                result.runIfNotHandled {
                    successOnce(result.data, result.end)
                }
            }
            is Result.Error -> {
                result.runIfNotHandled {
                    error(result.error)
                }
            }
            is Result.Loading -> {
                result.runIfNotHandled {
                    loading()
                }
            }
        }
    }

    /**
     * 可能会调用多次
     */
    open fun success(data: T, end: Boolean) {}

    /**
     * 以下方法只会调用一次
     */
    open fun successOnce(data: T, end: Boolean) {}

    open fun error(error: Throwable) {}

    open fun loading() {}
}