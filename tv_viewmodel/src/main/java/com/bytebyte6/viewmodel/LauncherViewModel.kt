package com.bytebyte6.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bytebyte6.common.*
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.User
import com.bytebyte6.usecase.InitAppUseCase

class LauncherViewModel(
    private val initAppUseCase: InitAppUseCase
) : BaseViewModel() {
    fun start(): LiveData<Result<User>> {
        addDisposable(
                initAppUseCase.execute(Unit).onSingle()
        )
        return initAppUseCase.result()
    }
}