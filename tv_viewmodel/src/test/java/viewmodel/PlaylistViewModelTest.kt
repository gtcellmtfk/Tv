package viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.common.Result
import com.bytebyte6.common.getSuccessData
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.usecase.DownloadTvUseCase
import com.bytebyte6.usecase.SearchTvLogoParam
import com.bytebyte6.usecase.SearchTvLogoUseCase
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
            FakeSearchTvLogoUseCase, DownloadTvUseCase(FakeDataManager), FakeDataManager
        )
        var tvs: List<Tv>? = null
        viewModel.setPlaylistId(FakeDataManager.playlist.playlistId)
        viewModel.playlistWithTvs.observeForever {
            tvs = it.tvs
        }
        assert(tvs == FakeDataManager.tvs2)
        viewModel.download(0,tvs!![0])
        val result = viewModel.updateTv.getSuccessData()
        assert(result!!.tv.download)
    }

    object FakeSearchTvLogoUseCase : SearchTvLogoUseCase {
        val RESULT: MutableLiveData<Result<SearchTvLogoParam>> = MutableLiveData()

        override fun run(tvLogoParam: SearchTvLogoParam): SearchTvLogoParam {
            return SearchTvLogoParam(FakeDataManager.tvs2)
        }
    }
}