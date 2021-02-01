package com.bytebyte6.viewmodel

import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.data.AppDatabase
import com.bytebyte6.data.dataModule
import com.bytebyte6.data.roomMemoryModule
import com.bytebyte6.usecase.useCaseModule
import com.bytebyte6.dependency.isError
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
            modules(roomMemoryModule, dataModule, useCaseModule,
                viewModule
            )
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
}