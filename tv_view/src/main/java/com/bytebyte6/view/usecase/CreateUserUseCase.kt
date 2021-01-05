package com.bytebyte6.view.usecase

import com.bytebyte6.data.RxUseCase
import com.bytebyte6.data.dao.UserDao
import com.bytebyte6.data.entity.User
import io.reactivex.rxjava3.core.Single

class CreateUserUseCase(
    private val userDao: UserDao
) : RxUseCase<User, Boolean>() {
    override fun getSingle(param: User): Single<Boolean> {
        return Single.create {
            if (userDao.count() == 0) {
                userDao.insert(param)
            }
            it.onSuccess(true)
        }
    }
}