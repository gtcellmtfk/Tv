package com.bytebyte6.usecase

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.data.AppDatabase
import com.bytebyte6.data.dataModule
import com.bytebyte6.data.entity.Tv
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
        db.userDao().insert(User(name = "Admin"))
        val tv1 = Tv(name = "A", url = "A.url")
        val tv2 = Tv(name = "B", url = "B.url")
        val tv3 = Tv(name = "C", url = "C.url")
        val tv4 = Tv(name = "D", url = "D.url")
        val tv5 = Tv(name = "E", url = "E.url")
        val list = mutableListOf(tv1, tv3, tv4)
        db.tvDao().insert(list)
        val all = mutableListOf(tv1, tv2, tv3, tv4, tv5)
        parseM3uUseCase.tvsFromFile = all
        val playlistId = parseM3uUseCase.run(Uri.parse("")).playlistId
        val playlistWithTvsById = db.playlistDao().getPlaylistWithTvsById(playlistId)
        assert(playlistWithTvsById.tvs.size == 5)
    }
}