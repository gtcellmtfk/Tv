package com.bytebyte6.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.common.*
import com.bytebyte6.common.test.getAwaitValue
import com.bytebyte6.common.test.observeForTesting
import com.bytebyte6.data.dataModule
import com.bytebyte6.data.entity.Tv

import com.bytebyte6.usecase.useCaseModule
import com.bytebyte6.data.DataManager
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
    private val dataManager by inject<DataManager>()

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
        dataManager.insertTv(cctvs)
        assert(dataManager.getTvs().size == 5)
    }

    @Test
    fun test_search_cctv_fav() {
        viewModel.search("CCTV")
        viewModel.fav(0)
        viewModel.favoriteResult.observeForTesting {
            assert(viewModel.favoriteResult.getAwaitValue()!!.isSuccess()!!.tv.favorite)
        }
    }

    @Test
    fun test_search_cctv() {
        viewModel.search("CCTV")
        viewModel.searchResult.observeForTesting {
            assert(viewModel.searchResult.getAwaitValue()!!.size == 5)
        }
    }

    @Test
    fun test_search_null() {
        viewModel.search(null)
        assert(viewModel.searchResult.value==null)
        viewModel.search("")
        assert(viewModel.searchResult.value==null)
    }
}