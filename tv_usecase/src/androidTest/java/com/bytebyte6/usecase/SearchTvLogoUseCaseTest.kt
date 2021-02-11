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
class SearchTvLogoUseCaseTest : KoinTest {

    private val dataManager:DataManager by inject()
    private val searchTvLogoUseCase: SearchTvLogoUseCase by inject()

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
    fun test_name_not_empty_logo_empty() {
        dataManager.insertTv(Tv(url = "A",name = "A"))
        val param = SearchTvLogoParam(dataManager.getTvs())
        searchTvLogoUseCase.execute(param).test().assertValue(param)
        assert(param.tvs[0].logo.isNotEmpty())
    }

    @Test
    fun test_name_empty_logo_empty() {
        dataManager.insertTv(Tv(url = "A",name = ""))
        val param = SearchTvLogoParam(dataManager.getTvs())
        searchTvLogoUseCase.execute(param).test().assertValue(param)
        assert(param.tvs[0].logo.isEmpty())
    }
}