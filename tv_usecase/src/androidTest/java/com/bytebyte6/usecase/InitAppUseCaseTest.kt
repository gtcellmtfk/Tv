package com.bytebyte6.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.dataModule
import com.bytebyte6.data.entity.User
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
class InitAppUseCaseTest : KoinTest {

    private val dataManager: DataManager by inject()
    private val initAppUseCase: InitAppUseCase by inject()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun start() {
        startKoin {
            modules(roomMemoryModule, dataModule, useCaseModule)
        }
    }

    @After
    fun stop() {
        stopKoin()
    }

    @Test
    fun test() {
        initAppUseCase.execute(Unit).test().assertValue {
            it.name == "Admin"
        }
        assert(dataManager.getCountryCount() != 0)
        assert(dataManager.getLangCount() != 0)
        assert(dataManager.getCategoryCount() != 0)
        assert(dataManager.getUsers().isNotEmpty())
    }
}