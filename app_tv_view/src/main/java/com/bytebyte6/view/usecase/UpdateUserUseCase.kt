package com.bytebyte6.view.usecase

import com.bytebyte6.data.RxSingleUseCase
import com.bytebyte6.data.dao.UserDao
import com.bytebyte6.data.entity.User
import io.reactivex.rxjava3.core.Single

class UpdateUserUseCase(private val userDao: UserDao) : RxSingleUseCase<User,Boolean> (){
    override fun getSingle(param: User): Single<Boolean> {
        return Single.create{
            userDao.update(param)
            it.onSuccess(true)
        }
    }
}