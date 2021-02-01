package com.bytebyte6.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.base.getSuccessData
import com.bytebyte6.base.isLoading
import com.bytebyte6.data.AppDatabase
import com.bytebyte6.data.dataModule
import com.bytebyte6.data.roomMemoryModule
import com.bytebyte6.usecase.DownloadListUseCase
import com.bytebyte6.usecase.UpdateTvUseCase
import com.bytebyte6.usecase.testExoPlayerModule
import com.bytebyte6.usecase.useCaseModule
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.koin.test.inject


@RunWith(AndroidJUnit4::class)
class DownloadViewModelTest : AutoCloseKoinTest() {

    private val appDatabase by inject<AppDatabase>()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun start() {
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
    }

    @Test
    fun test_pauseInterval() {
        val viewModel = getViewModel()
        var loading=false
        viewModel.downloadListResult.observeForever {
            if (it.isLoading()){
                loading=true
            }
        }
        viewModel.pauseInterval()
        assert(loading)
        assert(viewModel.downloadListResult.getSuccessData()!!.isEmpty())
        assert(!viewModel.isStartInterval())
    }

    @Test
    fun test_startInterval(){
        val viewModel=getViewModel()
        viewModel.startInterval()
        assert(!viewModel.isStartInterval())
    }

    @Test
    fun test_loadDownloadList() {
        val viewModel = getViewModel()
        var loading=false
        viewModel.downloadListResult.observeForever {
            if (it.isLoading()){
                loading=true
            }
        }
        viewModel.loadDownloadList()
        assert(loading)
        assert(viewModel.downloadListResult.getSuccessData()!!.isEmpty())
        assert(!viewModel.isStartInterval())
    }


    @Test
    fun test_delete() {
        val viewModel = getViewModel()
        viewModel.deleteDownload(0)
        assert(viewModel.deleteResult.value == null)
    }

    private fun getViewModel(): DownloadViewModel {
        return DownloadViewModel(
            DownloadListUseCase(appDatabase.tvDao(), get()),
            UpdateTvUseCase(appDatabase.tvDao())
        )
    }
}