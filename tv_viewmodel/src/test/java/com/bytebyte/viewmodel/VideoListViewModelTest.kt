package com.bytebyte.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bytebyte6.common.getSuccessData
import com.bytebyte6.common.isLoading
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.image.SearchImageImpl
import com.bytebyte6.usecase.SearchTvLogoUseCaseImpl
import com.bytebyte6.usecase.FavoriteTvUseCase
import com.bytebyte6.viewmodel.VideoListViewModel
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class VideoListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var tvs: MutableList<Tv>

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }
        RxJavaPlugins.setSingleSchedulerHandler {
            Schedulers.trampoline()
        }
        tvs = mutableListOf()
        for (i in 0..100) {
            tvs.add(Tv(name = "CCTV $i", url = "CCTV URL.$i", logo = "CCTV"))
        }
        FakeDataManager.insertTv(tvs)
    }

    @Test
    fun test() {
        val viewModel = VideoListViewModel(
            FakeDataManager,
            SearchTvLogoUseCaseImpl(SearchImageImpl(), FakeDataManager),
            FavoriteTvUseCase(FakeDataManager)
        )

        //测试加载
        viewModel.setKey("CCTV")
        var loading = false
        viewModel.tvs.observeForever {
            if (it.isLoading()) {
                loading = true
            }
        }
        viewModel.loadMore()
        assert(loading)
        loading = false
        assert(viewModel.tvs.getSuccessData()!!.size == 50)
        viewModel.loadMore()
        assert(loading)
        loading = false
        assert(viewModel.tvs.getSuccessData()!!.size == 100)
        viewModel.loadMore()
        assert(loading)
        loading = false
        assert(viewModel.tvs.getSuccessData()!!.size == 101)

        //测试收藏
        viewModel.fav(0)
        assert(viewModel.favoriteResult.getSuccessData()!!.tv.favorite)
    }

}