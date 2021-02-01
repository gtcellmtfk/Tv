package com.bytebyte6.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.data.dataModule
import com.bytebyte6.usecase.useCaseModule
import com.bytebyte6.common.emit
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class HomeViewModelTest : AutoCloseKoinTest() {

    private val viewModel by inject<HomeViewModel>()

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
    }

    @Test
    fun test_refresh() {
        val countDownLatch = CountDownLatch(1)
        var loadingCount = 0
        var successCount = 0
        var errorCount = 0
        viewModel.tvRefresh.observeForever {
            it.emit(
                {
                    successCount++
                    countDownLatch.countDown()
                }, {
                    errorCount++
                    countDownLatch.countDown()
                }, {
                    loadingCount++
                }
            )
        }
        viewModel.refresh()
        countDownLatch.await(5, TimeUnit.SECONDS)
        assert(loadingCount == 1)
        assert(successCount == 1)
        assert(errorCount == 0)
    }
}