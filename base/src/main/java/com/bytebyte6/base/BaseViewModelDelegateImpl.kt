package com.bytebyte6.base


import androidx.lifecycle.MutableLiveData

class BaseViewModelDelegateImpl : BaseViewModelDelegate {

    //进度条
    override val loading by lazy { MutableLiveData<Event<Boolean>>() }

    //Toast
    override val toast by lazy { MutableLiveData<Event<Message>>() }

    //snack bar
    override val snackBar by lazy { MutableLiveData<Event<Message>>() }

    override fun postLoading(loading: Boolean) {
        this.loading.postValue(Event(loading))
    }

    override fun postToast(message: Message) {
        toast.postValue(Event(message))
    }

    override fun postSnack(message: Message) {
        snackBar.postValue(Event(message))
    }

}