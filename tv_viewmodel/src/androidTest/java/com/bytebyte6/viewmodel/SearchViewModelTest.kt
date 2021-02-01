package com.bytebyte6.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.data.dataModule
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.roomMemoryModule
import com.bytebyte6.usecase.testExoPlayerModule
import com.bytebyte6.usecase.useCaseModule
import com.bytebyte6.base.getSuccessData
import com.bytebyte6.base.isLoading
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
class SearchViewModelTest : AutoCloseKoinTest() {

    private val viewModel by inject<SearchViewModel>()
    private val tvDao by inject<TvDao>()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun start() {
        stopKoin()
        startKoin {
            modules(roomMemoryModule, dataModule, useCaseModule,
                viewModule, testExoPlayerModule)
        }
        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }
        val cctvs = mutableListOf<Tv>()
        cctvs.add(Tv(name = "CCTV 1", url = "CCTV 1"))
        cctvs.add(Tv(name = "CCTV 2", url = "CCTV 2"))
        cctvs.add(Tv(name = "CCTV 3", url = "CCTV 3"))
        cctvs.add(Tv(name = "CCTV 4", url = "CCTV 4"))
        cctvs.add(Tv(name = "CCTV 5", url = "CCTV 5"))
        tvDao.insert(cctvs)
        assert(tvDao.getTvs().size == 5)
    }

    @Test
    fun test_search_cctv_fav() {
        var isLoading = false
        viewModel.searchResult.observeForever {
            if (it.isLoading()) {
                isLoading = true
            }
        }
        viewModel.search("CCTV")
        assert(isLoading)
        val value = viewModel.searchResult.getSuccessData()
        assert(value != null && value.size == 5)
        viewModel.fav(0)
        assert(viewModel.favoriteResult.getSuccessData()!!.tv.favorite)
    }

    @Test
    fun test_search_cctv() {
        var isLoading = false
        viewModel.searchResult.observeForever {
            if (it.isLoading()) {
                isLoading = true
            }
        }
        viewModel.search("CCTV")
        assert(isLoading)
        val value = viewModel.searchResult.getSuccessData()
        assert(value != null && value.size == 5)
    }

    @Test
    fun test_search_null() {
        var isLoading = false
        viewModel.logoUrlSearchResult.observeForever {
            if (it.isLoading()) {
                isLoading = true
            }
        }
        viewModel.search(null)
        assert(!isLoading)
        viewModel.search("")
        assert(!isLoading)
    }

}