package com.bytebyte6.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.data.AppDatabase
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
class SearchTvUseCaseTest : KoinTest {

    private val db: AppDatabase by inject()
    private val searchTvUseCase: SearchTvUseCase by inject()

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
        searchTvUseCase.execute("").test().assertValue {
            it.isEmpty()
        }
        db.tvDao().insert(Tv(url="A"))
        db.tvDao().insert(Tv(url="B"))
        db.tvDao().insert(Tv(url="C"))
        searchTvUseCase.execute("A").test().assertValue {
            it.size==1
        }
        searchTvUseCase.execute("B").test().assertValue {
            it.size==1
        }
        searchTvUseCase.execute("C").test().assertValue {
            it.size==1
        }
    }
}