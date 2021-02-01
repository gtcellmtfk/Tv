package com.bytebyte6.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.data.AppDatabase
import com.bytebyte6.data.dataModule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
class TvRefreshUseCaseTest : KoinTest {

    private val db: AppDatabase by inject()
    private val tvRefreshUseCase: TvRefreshUseCase by inject()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        startKoin {
            modules(roomMemoryModule, dataModule, useCaseModule)
        }
    }

    @After
    fun closeDb() {
        db.close()
        stopKoin()
    }

    @Test
    fun test() {
        tvRefreshUseCase.getSingle().test().assertNoErrors()
    }
}