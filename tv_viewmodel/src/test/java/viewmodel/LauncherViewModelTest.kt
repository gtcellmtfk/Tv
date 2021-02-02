package viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.common.Result
import com.bytebyte6.common.getError
import com.bytebyte6.common.getSuccessData
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.usecase.CreateUserUseCase
import com.bytebyte6.usecase.InitDataUseCase
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
    }

    @Test
    fun test() {
        val createUserUseCase = CreateUserUseCase(FakeUserDao)
        val viewModel = LauncherViewModel(FakeInitDataUseCase, createUserUseCase)
        val user = createUserUseCase.result().getSuccessData()
        assert(user == FakeUserDao.defaultUser)
        val successData = viewModel.start().getSuccessData()
        assert(successData != null && successData[0] == FakeTvDao.defaultTv)
        FakeInitDataUseCase.error = true
        val error = viewModel.start().getError()
        assert(error != null && error.message == "Error")
        FakeInitDataUseCase.error = false
    }

    object FakeInitDataUseCase : InitDataUseCase {
        var error = false

        override fun run(param: Unit): List<Tv> {
            return if (error)
                throw Exception("Error")
            else
                listOf(FakeTvDao.defaultTv)
        }

        override val result: MutableLiveData<Result<List<Tv>>> = MutableLiveData()
    }
}