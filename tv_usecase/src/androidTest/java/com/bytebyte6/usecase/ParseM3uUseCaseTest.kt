package com.bytebyte6.usecase

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.dataModule
import com.bytebyte6.data.entity.Tv
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
import java.io.FileNotFoundException

@RunWith(AndroidJUnit4::class)
class ParseM3uUseCaseTest : KoinTest {

    private val dataManager: DataManager by inject()
    private val parseM3uUseCase: ParseM3uUseCase by inject()
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
    fun test_parse_error() {
        parseM3uUseCase.execute(ParseParam(Uri.EMPTY))
                .test().assertError(Exception::class.java)

        parseM3uUseCase.execute(ParseParam(assetsFileName = "1"))
                .test().assertError(FileNotFoundException::class.java)
    }

    @Test
    fun test_parse_success() {
//        initAppUseCase.execute(Unit).test().assertValue {
//            it.name == "Admin"
//        }
        val tv1 = Tv(name = "A", url = "A.url", countryCode = "ar")
        val tv2 = Tv(name = "B", url = "B.url", countryCode = "in")
        val tv3 = Tv(name = "C", url = "C.url", countryCode = "cn")
        val tv4 = Tv(name = "D", url = "D.url", countryCode = "in")
        val tv5 = Tv(name = "E", url = "E.url", countryCode = "ar")
        val all = mutableListOf(tv1, tv2, tv3, tv4, tv5)
        val playlistId = parseM3uUseCase.run(ParseParam(forTest = all)).playlistId
        val playlistWithTvs = dataManager.getPlaylistWithTvs(playlistId)
        val userWithPlaylist = dataManager.getUserWithPlaylist()
        assert(userWithPlaylist.playlists.size == 1)
        assert(playlistWithTvs.playlist.playlistName == "forTest.m3u")
        assert(playlistWithTvs.tvs.size == 5)
    }
}