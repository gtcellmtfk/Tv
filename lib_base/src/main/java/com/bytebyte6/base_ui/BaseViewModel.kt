package com.bytebyte6.base_ui

import androidx.lifecycle.ViewModel
import com.bytebyte6.base.logd
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    init {
        logd("init...${this}")
    }

    private val compositeDisposable = CompositeDisposable()

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        logd("onCleared...${this}")
        compositeDisposable.dispose()
        super.onCleared()
    }
}