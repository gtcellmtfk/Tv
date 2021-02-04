package viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.common.Result
import com.bytebyte6.common.getError
import com.bytebyte6.common.getSuccessData
import com.bytebyte6.data.entity.Tv
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
    }

    @Test
    fun test() {
        val viewModel = LauncherViewModel(FakeDataManager,FakeInitAppUseCase)
        val successData = viewModel.start().getSuccessData()
        assert(successData != null && successData[0] == FakeTvDao.defaultTv)
        FakeInitAppUseCase.error = true
        val error = viewModel.start().getError()
        assert(error != null && error.message == "Error")
        FakeInitAppUseCase.error = false
    }

    object FakeInitAppUseCase : InitAppUseCase {
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