package com.bytebyte6.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    //进度条
    private val loading by lazy { MutableLiveData<Event<Boolean>>() }
    fun getLoadingLiveData(): LiveData<Event<Boolean>> {
        return loading
    }

    //Toast
    private val toast by lazy { MutableLiveData<Event<Message>>() }
    fun getToastLiveData(): LiveData<Event<Message>> {
        return toast
    }

    //snack bar
    private val snackBar by lazy { MutableLiveData<Event<Message>>() }
    fun getSnackBarLiveData(): LiveData<Event<Message>> {
        return snackBar
    }

    private val compositeDisposable: CompositeDisposable? by lazy { CompositeDisposable() }

    protected fun isLoading(): Boolean {
        return if (loading.value == null) false else loading.value!!.peekContent()
    }

    protected fun showLoading(showLoading: Boolean) {
        loading.postValue(Event(showLoading))
    }

    protected fun showToast(message: Message) {
        toast.postValue(Event(message))
    }

    protected fun showSnackBar(message: Message) {
        snackBar.postValue(Event(message))
    }

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable?.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable?.dispose()
        super.onCleared()
    }
}