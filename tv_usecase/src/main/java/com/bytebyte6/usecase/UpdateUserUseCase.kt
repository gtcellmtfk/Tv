package com.bytebyte6.usecase

import com.bytebyte6.common.RxUseCase
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.User

class UpdateUserUseCase(private val dataManager: DataManager) : RxUseCase<User, Boolean>() {
    override fun run(param: User): Boolean {
        dataManager.updateUser(param)
        return true
    }
}