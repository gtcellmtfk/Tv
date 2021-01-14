package com.bytebyte6.view.setting

import com.bytebyte6.base_ui.BaseViewModel
import com.bytebyte6.data.dao.UserDao
import com.bytebyte6.view.usecase.UpdateUserUseCase

class UserViewModel(
    private val dao: UserDao,
    private val updateUserUseCase: UpdateUserUseCase
) : BaseViewModel() {

    val user = dao.user()

    fun updateNight(nightMode: Boolean) {
        user.value?.apply {
            this.nightMode = nightMode
            addDisposable(
                updateUserUseCase.execute(this)
            )
        }

    }

    fun updateCapturePic(capturePic: Boolean) {
        user.value?.apply {
            this.capturePic = capturePic
            addDisposable(
                updateUserUseCase.execute(this)
            )
        }
    }
}