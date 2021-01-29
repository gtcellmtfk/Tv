package com.bytebyte6.view.me

import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.base.isError
import com.bytebyte6.base.isSuccess
import com.bytebyte6.data.AppDatabase
import com.bytebyte6.data.dataModule
import com.bytebyte6.data.roomMemoryModule
import com.bytebyte6.usecase.useCaseModule
import com.bytebyte6.view.viewModule
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
import java.util.*

@RunWith(AndroidJUnit4::class)
class MeViewModelTest : AutoCloseKoinTest() {

    private val context by inject<Context>()

    private val appDatabase by inject<AppDatabase>()

    private val viewModel by inject<MeViewModel>()

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
    fun test_parse_error() {
        viewModel.parseM3u(Uri.parse(UUID.randomUUID().toString()))
        assert(viewModel.parseResult.value!!.isError() != null)
    }

    @Test
    fun test_parse_success() {
        // 不知如何测试
        // Permission Denial: opening provider com.android.externalstorage.ExternalStorageProvider
        // from ProcessRecord{e783ddf 8546:com.bytebyte6.rtmp/u0a74} (pid=8546, uid=10074)
        // requires android.permission.MANAGE_DOCUMENTS or android.permission.MANAGE_DOCUMENTS
        val uri = "content://com.android.externalstorage.documents/document/primary%3Akor.m3u"
        viewModel.parseM3u(Uri.parse(uri))
        assert(viewModel.parseResult.value!!.isSuccess() != null)
    }
}