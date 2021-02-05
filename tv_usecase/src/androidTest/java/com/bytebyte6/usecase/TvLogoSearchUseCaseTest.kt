package com.bytebyte6.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import com.bytebyte6.data.DataManager
import com.bytebyte6.data.dataModule
import com.bytebyte6.data.entity.Tv
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
class TvLogoSearchUseCaseTest : KoinTest {

    private val dataManager:DataManager by inject()
    private val tvLogoSearchUseCase: TvLogoSearchUseCase by inject()

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
        stopKoin()
    }

    @Test
    fun test() {
        val id = dataManager.insertTv(Tv(url = "A"))
        val tv = Tv(id, url = "A")
        tvLogoSearchUseCase.execute(SearchParam(tv.tvId,0)).test().assertValue(SearchParam(tv.tvId,0))
    }
}