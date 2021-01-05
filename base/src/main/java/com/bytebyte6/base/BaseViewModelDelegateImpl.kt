package com.bytebyte6.base


import androidx.lifecycle.MutableLiveData

class BaseViewModelDelegateImpl : BaseViewModelDelegate {

    //进度条
    override val loading by lazy { MutableLiveData<Event<Boolean>>() }

    //snack bar
    override val snackBar by lazy { MutableLiveData<Event<Message>>() }

    override fun postLoading(loading: Boolean) {
        this.loading.postValue(Event(loading))
    }

    override fun postSnack(message: Message) {
        snackBar.postValue(Event(message))
    }

}