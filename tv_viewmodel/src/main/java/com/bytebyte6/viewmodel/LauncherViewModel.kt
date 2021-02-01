package com.bytebyte6.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bytebyte6.common.Result
import com.bytebyte6.common.isSuccess
import com.bytebyte6.common.BaseViewModel
import com.bytebyte6.common.onIo
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.User
import com.bytebyte6.usecase.CreateUserUseCase
import com.bytebyte6.usecase.InitDataUseCase

class LauncherViewModel(
    private val initDataUseCase: InitDataUseCase,
    private val createUserUseCase: CreateUserUseCase
) : BaseViewModel() {

    private val observer = Observer<Result<User>> {
        it.isSuccess()?.apply {
            AppCompatDelegate.setDefaultNightMode(
                if (nightMode)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO
            )
        }
        removeObserver()
    }

    private fun removeObserver() {
        createUserUseCase.result().removeObserver(observer)
    }

    init {
        //1、没有用户就创建用户
        addDisposable(
            createUserUseCase.execute(User(name = "Admin")).onIo()
        )
        createUserUseCase.result().observeForever(observer)
    }

    fun start(): LiveData<Result<List<Tv>>> {
        addDisposable(
            initDataUseCase.execute(Unit).onIo()
        )
        return initDataUseCase.result()
    }
}