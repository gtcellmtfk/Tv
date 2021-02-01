package com.bytebyte6.usecase

import com.bytebyte6.common.RxUseCase
import com.bytebyte6.data.dao.UserDao
import com.bytebyte6.data.entity.User

class UpdateUserUseCase(private val userDao: UserDao) : RxUseCase<User, Boolean>() {
    override fun run(param: User): Boolean {
        userDao.update(param)
        return true
    }
}