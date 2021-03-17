package com.example.test_ui_factory.usecase

import com.bytebyte6.common.RxUseCase
import com.example.test_ui_factory.dao.UserDao
import com.example.test_ui_factory.entry.User

class InsertUserUseCase(
    private val dao: UserDao
) : RxUseCase<User, User>() {
    override fun run(param: User): User {
        dao.insert(param)
        return param
    }
}

class DeleteUserUseCase(
    private val dao: UserDao
) : RxUseCase<User, User>() {
    override fun run(param: User): User {
        dao.delete(param)
        return param
    }
}

class UpdateUserUseCase(
    private val dao: UserDao
) : RxUseCase<User, User>() {
    override fun run(param: User): User {
        dao.update(param)
        return param
    }
}