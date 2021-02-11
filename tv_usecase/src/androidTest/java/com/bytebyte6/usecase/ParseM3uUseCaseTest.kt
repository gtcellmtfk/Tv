package com.bytebyte6.usecase

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.dataModule
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.User
import com.bytebyte6.lib_test.assertError
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

    private val dataManager: DataManager by inject()
    private val parseM3uUseCase: ParseM3uUseCase by inject()

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
        dataManager.insertUser(User(name = "Admin"))
        val tv1 = Tv(name = "A", url = "A.url")
        val tv2 = Tv(name = "B", url = "B.url")
        val tv3 = Tv(name = "C", url = "C.url")
        val tv4 = Tv(name = "D", url = "D.url")
        val tv5 = Tv(name = "E", url = "E.url")
        val list = mutableListOf(tv1, tv3, tv4)
        dataManager.insertTv(list)
        val all = mutableListOf(tv1, tv2, tv3, tv4, tv5)
        parseM3uUseCase.setTvs(all)
        val playlistId = parseM3uUseCase.run(Uri.parse("test.m3u")).playlistId
        val playlistWithTvsById = dataManager.getPlaylistWithTvs(playlistId)
        val userWithPlaylist = dataManager.getUserWithPlaylist()
        assert(userWithPlaylist.playlists.size == 1)
        assert(playlistWithTvsById.tvs.size == 5)

        assertError {
            parseM3uUseCase.run(Uri.EMPTY)
        }
    }
}