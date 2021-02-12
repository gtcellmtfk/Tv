package com.bytebyte6.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bytebyte6.common.BaseViewModel
import com.bytebyte6.common.Result
import com.bytebyte6.common.onComputation
import com.bytebyte6.common.onIo
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.User
import com.bytebyte6.usecase.InitAppUseCase

class LauncherViewModel(
    private val initAppUseCase: InitAppUseCase
) : BaseViewModel() {
    fun start(): LiveData<Result<User>> {
        addDisposable(
            initAppUseCase.execute(Unit).onComputation()
        )
        return initAppUseCase.result()
    }
}