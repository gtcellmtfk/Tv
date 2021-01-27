package com.bytebyte6.view

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.base.Result
import com.bytebyte6.base.getSuccessData
import com.bytebyte6.base.isSuccess
import com.bytebyte6.data.AppDatabase
import com.bytebyte6.data.dataModule
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.roomMemoryModule
import com.bytebyte6.usecase.DownloadListUseCase
import com.bytebyte6.usecase.TvAndDownload
import com.bytebyte6.usecase.UpdateTvUseCase
import com.bytebyte6.usecase.useCaseModule
import com.bytebyte6.view.download.DownloadServicePro
import com.bytebyte6.view.download.DownloadViewModel
import com.google.android.exoplayer2.offline.DownloadManager
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.koin.test.inject


@RunWith(AndroidJUnit4::class)
class DownloadViewModelTest : AutoCloseKoinTest() {

    private val context by inject<Context>()

    private val appDatabase by inject<AppDatabase>()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun start() {
        stopKoin()
        startKoin {
            modules(roomMemoryModule, dataModule, useCaseModule, viewModule)
        }
        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }
    }

    @Test
    fun test_download_use_nice_url() {
        val viewModel=getViewModel()
        val url = "https://iqiyi.zuidameiju.com/20200219/10862_9ae7e54e/index.m3u8"
        val tv = Tv(url = url).apply {
            download = true
        }
        appDatabase.tvDao().insert(tv)
        DownloadServicePro.addDownload(context, url)
        viewModel.loadDownloadList()
        val value = viewModel.downloadList.getSuccessData()
        assert(value != null)
        assert(value!!.isNotEmpty())
    }

    @Test
    fun test_download_use_error_url() {
        val viewModel=getViewModel()
        val url = "//iqiyi.zuidameiju.com/10862_9ae7e54e/ide.m3u8"
        val tv = Tv(url = url).apply {
            download = true
        }
        appDatabase.tvDao().insert(tv)
        DownloadServicePro.addDownload(context, url)
        viewModel.loadDownloadList()
        val value = viewModel.downloadList.value!!
        assert(value.isSuccess() != null)
        assert(value.isSuccess()!!.isEmpty())
    }

    @Test
    fun test_delete() {
        val viewModel=getViewModel()
        test_download_use_nice_url()
        viewModel.deleteDownload(0)
        val value: Result<List<TvAndDownload>> = viewModel.downloadList.value!!
        assert(value.isSuccess() != null)
        assert(value.isSuccess()!!.isEmpty())
    }

    private fun getViewModel(): DownloadViewModel {
        return DownloadViewModel(
            DownloadListUseCase(appDatabase.tvDao(), get()),
            UpdateTvUseCase(appDatabase.tvDao())
        )
    }
}