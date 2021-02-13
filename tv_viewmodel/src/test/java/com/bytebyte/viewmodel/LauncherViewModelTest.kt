package com.bytebyte.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.common.Result
import com.bytebyte6.common.getError
import com.bytebyte6.common.getSuccessData
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.User
import com.bytebyte6.testdata.defaultUser
import com.bytebyte6.testdata.tv1
import com.bytebyte6.usecase.InitAppUseCase
import com.bytebyte6.viewmodel.LauncherViewModel
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LauncherViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }
        RxJavaPlugins.setComputationSchedulerHandler {
            Schedulers.trampoline()
        }
    }

    @Test
    fun test() {
        val viewModel = LauncherViewModel(FakeInitAppUseCase)
        val successData = viewModel.start().getSuccessData()
        assert(successData != null && successData == defaultUser)
        FakeInitAppUseCase.error = true
        val error = viewModel.start().getError()
        assert(error != null && error.message == "Error")
        FakeInitAppUseCase.error = false
    }

    object FakeInitAppUseCase : InitAppUseCase {
        var error = false

        override fun run(param: Unit): User {
            return if (error)
                throw Exception("Error")
            else
                defaultUser
        }

        override val result: MutableLiveData<Result<User>> = MutableLiveData()
    }
}