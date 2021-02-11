package com.bytebyte6.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.common.*
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.PAGE_SIZE
import com.bytebyte6.data.dataModule
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.lib_test.assertError
import com.bytebyte6.lib_test.getAwaitValue
import com.bytebyte6.lib_test.observeForTesting
import com.bytebyte6.usecase.useCaseModule
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
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

    private val viewModel by inject<SearchViewModel2>()
    private val dataManager by inject<DataManager>()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var pageSize = 0

    @Before
    fun start() {
        pageSize = PAGE_SIZE
        PAGE_SIZE = 20
        stopKoin()
        startKoin {
            modules(
                roomMemoryModule, dataModule, useCaseModule,
                viewModule, testExoPlayerModule
            )
        }
        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }
        RxJavaPlugins.setSingleSchedulerHandler {
            Schedulers.trampoline()
        }
        val cctvs = mutableListOf<Tv>()
        for (i in 0..100)
            cctvs.add(Tv(name = "CCTV $i", url = "CCTV $i", logo = "CCTV"))
        dataManager.insertTv(cctvs)
        assert(dataManager.getTvs().size == 101)
    }

    @After
    fun reset() {
        PAGE_SIZE = pageSize
    }

    @Test
    fun test_loadMore() {
        viewModel.search("CCTV")
        val searchResult = viewModel.searchResult
        assert(searchResult.getSuccessData()!!.size == PAGE_SIZE)
        viewModel.loadMore()
        assert(searchResult.getSuccessData()!!.size == PAGE_SIZE * 2)
        viewModel.loadMore()
        assert(searchResult.getSuccessData()!!.size == PAGE_SIZE * 3)
        viewModel.loadMore()
        assert(searchResult.getSuccessData()!!.size == PAGE_SIZE * 4)
        viewModel.loadMore()
        assert(searchResult.getSuccessData()!!.size == PAGE_SIZE * 5)
        viewModel.loadMore()
        assert(searchResult.getSuccessData()!!.size == 101)
        assert(searchResult.end())
        viewModel.loadMore()
        assert(searchResult.getError() is NoMoreData)
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
            assert(viewModel.searchResult.getSuccessData()!!.size == PAGE_SIZE)
        }
    }

    @Test
    fun test_search_null() {
        viewModel.search(null)
        assert(viewModel.searchResult.value == null)
        viewModel.search("")
        assert(viewModel.searchResult.value == null)
    }
}