package viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.common.Result
import com.bytebyte6.common.getSuccessData
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.usecase.SearchParam
import com.bytebyte6.usecase.TvLogoSearchUseCase
import com.bytebyte6.usecase.UpdateTvUseCase
import com.bytebyte6.viewmodel.PlaylistViewModel
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PlaylistViewModelTest {
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
        val viewModel = PlaylistViewModel(
            FakeTvLogoSearchUseCase, UpdateTvUseCase(FakeDataManager), FakeDataManager
        )
        var tvs: List<Tv>? = null
        viewModel.tvs(FakeDataManager.playlist.playlistId).observeForever {
            tvs = it
        }
        assert(tvs == FakeDataManager.tvs2)
        viewModel.download(0)
        val result = viewModel.updateTv.getSuccessData()
        assert(result!!.tv.download)
    }

    object FakeTvLogoSearchUseCase : TvLogoSearchUseCase {
        override val result: MutableLiveData<Result<SearchParam>> = MutableLiveData()

        override fun run(param: SearchParam): SearchParam {
            return SearchParam(1, 0)
        }
    }
}