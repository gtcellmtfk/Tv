package com.bytebyte6.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bytebyte6.common.BaseViewModel
import com.bytebyte6.common.Result
import com.bytebyte6.common.onIo
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.User
import com.bytebyte6.usecase.InitAppUseCase

class LauncherViewModel(
    dataManager: DataManager,
    private val initAppUseCase: InitAppUseCase
) : BaseViewModel() {

    private val observer = object : Observer<User> {
        override fun onChanged(user1: User) {
            user1.let {
                AppCompatDelegate.setDefaultNightMode(
                    if (it.nightMode) AppCompatDelegate.MODE_NIGHT_YES
                    else AppCompatDelegate.MODE_NIGHT_NO
                )
                user.removeObserver(this)
            }
        }
    }

    private val user = dataManager.user()

    fun obs() {
        user.observeForever(observer)
    }

    fun start(): LiveData<Result<List<Tv>>> {
        addDisposable(
            initAppUseCase.execute(Unit).onIo()
        )
        return initAppUseCase.result()
    }
}