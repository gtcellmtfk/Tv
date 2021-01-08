package com.bytebyte6.view

import androidx.lifecycle.LiveData
import com.bytebyte6.base.BaseViewModel
import com.bytebyte6.base.Event
import com.bytebyte6.base.mvi.Result
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.User
import com.bytebyte6.view.usecase.AppInitUseCase
import com.bytebyte6.view.usecase.CreateUserUseCase

class LauncherViewModel(
    private val createUserUseCase: CreateUserUseCase,
    private val appInitUseCase: AppInitUseCase
) : BaseViewModel() {

    fun init(): LiveData<Event<Result<List<Tv>>>> {
        addDisposable(
            createUserUseCase.execute(User(name = "Admin"))
        )
        addDisposable(
            appInitUseCase.execute("init")
        )
        return appInitUseCase.eventLiveData()
    }
}