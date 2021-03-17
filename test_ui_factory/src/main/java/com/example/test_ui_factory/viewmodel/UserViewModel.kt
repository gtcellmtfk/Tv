package com.example.test_ui_factory.viewmodel

import com.bytebyte6.common.BaseViewModel
import com.bytebyte6.common.onIo
import com.example.test_ui_factory.dao.UserDao
import com.example.test_ui_factory.entry.User
import com.example.test_ui_factory.usecase.InsertUserUseCase
import com.example.test_ui_factory.usecase.DeleteUserUseCase
import com.example.test_ui_factory.usecase.UpdateUserUseCase

class UserViewModel(
    private val dao: UserDao,
    private val insertUserUseCase: InsertUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : BaseViewModel() {

    val all = dao.allAsLiveData()

    fun user(/*id:Long*/) = dao.asLiveDataById(/*id*/)

    val insertResult = insertUserUseCase.result()

    val deleteResult = deleteUserUseCase.result()

    val updateResult = updateUserUseCase.result()
    
    fun insert(){
        addDisposable(
            insertUserUseCase.execute(User()).onIo()
        )
    }

    fun delete(){
        addDisposable(
            deleteUserUseCase.execute(User()).onIo()
        )
    }

    fun update(){
        addDisposable(
            updateUserUseCase.execute(User()).onIo()
        )
    }
}