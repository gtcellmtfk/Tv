package com.bytebyte6.view

import androidx.lifecycle.ViewModel
import com.bytebyte6.data.entity.User
import com.bytebyte6.view.usecase.CreateUserUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainViewModel(
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun init() {
        compositeDisposable.add(
            createUserUseCase.execute(User(name = "Admin"))
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}