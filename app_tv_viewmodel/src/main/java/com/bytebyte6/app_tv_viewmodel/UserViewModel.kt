package com.bytebyte6.app_tv_viewmodel

import com.bytebyte6.base.BaseViewModel
import com.bytebyte6.base.onIo
import com.bytebyte6.data.dao.UserDao
import com.bytebyte6.usecase.UpdateUserUseCase

class UserViewModel(
    dao: UserDao,
    private val updateUserUseCase: UpdateUserUseCase
) : BaseViewModel() {

    val user = dao.user()

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