package viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bytebyte6.lib_test.getAwaitValue
import com.bytebyte6.lib_test.observeForTesting
import com.bytebyte6.usecase.UpdateUserUseCase
import com.bytebyte6.viewmodel.UserViewModel
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }
    }

    @Test
    fun test_user_not_empty() {
        FakeDataManager.getCurrentUserIfNotExistCreate()
        val viewModel = UserViewModel(FakeDataManager, UpdateUserUseCase(FakeDataManager))
        viewModel.updateNight(true)
        viewModel.user.observeForTesting {
            assert(viewModel.user.getAwaitValue()!!.nightMode)
        }
        viewModel.updateCapturePic(true)
        viewModel.user.observeForTesting {
            assert(viewModel.user.getAwaitValue()!!.capturePic)
        }
    }

    @Test
    fun test_user_empty() {
        val viewModel = UserViewModel(FakeDataManager, UpdateUserUseCase(FakeDataManager))
        viewModel.updateNight(true)
        viewModel.user.observeForTesting {
            assert(viewModel.user.getAwaitValue() == null)
        }
        viewModel.updateCapturePic(true)
        viewModel.user.observeForever {
            assert(viewModel.user.getAwaitValue() == null)
        }
    }
}