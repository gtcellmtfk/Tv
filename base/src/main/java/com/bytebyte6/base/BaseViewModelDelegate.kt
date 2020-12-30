package com.bytebyte6.base

import androidx.lifecycle.LiveData


interface BaseViewModelDelegate {
    //模糊进度条
    val loading: LiveData<Event<Boolean>>

    //Toast
    val toast: LiveData<Event<Message>>

    //snack bar
    val snackBar: LiveData<Event<Message>>

    fun isLoading(): Boolean {
        return if (loading.value == null) false else loading.value!!.peekContent()
    }

    fun postLoading(loading: Boolean)

    fun postToast(message: Message)

    fun postSnackBar(message: Message)

    fun <T> getDefaultLoadData(): LoadData<T> {
        return object : LoadData<T> {
            override fun start() {
                postLoading(true)
            }

            override fun success(data: T) {
                postLoading(false)
            }

            override fun fail(error: Throwable) {
                postLoading(false)
                postSnackBar(Message(id = ErrorUtils.getMessage(error)))
            }
        }
    }
}
