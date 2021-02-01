package com.bytebyte6.usecase

import android.net.Uri
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
class ParseM3uUseCaseTest : KoinTest {

    private val db: AppDatabase by inject()
    private val parseM3uUseCase: ParseM3uUseCase by inject()

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
        //无权访问
        val uri = "content://com.android.fileexplorer.myprovider/external_files/kor.m3u"
        parseM3uUseCase.execute(Uri.parse(uri)).test().assertNotComplete()
    }
}