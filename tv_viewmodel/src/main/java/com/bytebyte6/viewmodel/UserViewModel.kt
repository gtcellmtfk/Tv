package com.bytebyte6.viewmodel

import com.bytebyte6.common.BaseViewModel
import com.bytebyte6.common.onIo
import com.bytebyte6.data.DataManager
import com.bytebyte6.usecase.UpdateUserUseCase

class UserViewModel(
    dataManager: DataManager,
    private val updateUserUseCase: UpdateUserUseCase
) : BaseViewModel() {

    val user = dataManager.user()

    fun updateNight(nightMode: Boolean) {
        user.value?.apply {
            this.nightMode = nightMode
            addDisposable(
                updateUserUseCase.execute(this).onIo()
            )
        }
    }

    fun updateCapturePic(capturePic: Boolean) {
        user.value?.apply {
            this.capturePic = capturePic
            addDisposable(
                updateUserUseCase.execute(this).onIo()
            )
        }
    }
}