package com.bytebyte6.base

import androidx.lifecycle.LiveData


interface BaseViewModelDelegate {

    //模糊进度条
    val loading: LiveData<Event<Boolean>>

    //snack bar
    val snackBar: LiveData<Event<Message>>

    fun isLoading(): Boolean {
        return if (loading.value == null) false else loading.value!!.peekContent()
    }

    fun postLoading(loading: Boolean)

    fun postSnack(message: Message)

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
                postSnack(Message(id = ErrorUtils.getMessage(error)))
            }
        }
    }
}
