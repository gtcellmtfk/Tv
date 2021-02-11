package com.bytebyte6.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.common.NoMoreData
import com.bytebyte6.common.end
import com.bytebyte6.common.getError
import com.bytebyte6.common.getSuccessData
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.PAGE_SIZE
import com.bytebyte6.data.dataModule
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.PlaylistTvCrossRef
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.UserPlaylistCrossRef
import com.bytebyte6.usecase.useCaseModule
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
class PlaylistViewModelTest : AutoCloseKoinTest() {

    private val viewModel by inject<PlaylistViewModel>()
    private val dataManager by inject<DataManager>()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var pageSize = 0

    private lateinit var playlist: Playlist

    @Before
    fun start() {
        pageSize = PAGE_SIZE
        PAGE_SIZE = 20
        stopKoin()
        startKoin {
            modules(
                roomMemoryModule, dataModule, useCaseModule,
                viewModule, testExoPlayerModule
            )
        }
        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }
        RxJavaPlugins.setSingleSchedulerHandler {
            Schedulers.trampoline()
        }
        val cctvs = mutableListOf<Tv>()
        for (i in 0..100)
            cctvs.add(Tv(name = "CCTV $i", url = "CCTV $i", logo = "CCTV"))
        dataManager.insertTv(cctvs)
        assert(dataManager.getTvs().size == 101)
        val user = dataManager.getCurrentUserIfNotExistCreate()
        val tvsFromDb = dataManager.getTvs()
        playlist = Playlist(playlistName = "P1")
        dataManager.insertPlaylist(playlist)
        playlist = dataManager.getPlaylists()[0]
        dataManager.crossRefUserWithPlaylist(UserPlaylistCrossRef(user.userId, playlist.playlistId))
        val list = mutableListOf<PlaylistTvCrossRef>()
        tvsFromDb.forEach {
            list.add(PlaylistTvCrossRef(playlist.playlistId, it.tvId))
        }
        dataManager.crossRefPlaylistWithTv(list)
    }

    @After
    fun reset() {
        PAGE_SIZE = pageSize
    }

    @Test
    fun test_loadMore() {
        viewModel.playlistId = playlist.playlistId
        viewModel.loadMore()

        assert(viewModel.count.value == 101)

        val result = viewModel.tvs
        assert(result.getSuccessData()!!.size == PAGE_SIZE)
        viewModel.loadMore()
        assert(result.getSuccessData()!!.size == PAGE_SIZE * 2)
        viewModel.loadMore()
        assert(result.getSuccessData()!!.size == PAGE_SIZE * 3)
        viewModel.loadMore()
        assert(result.getSuccessData()!!.size == PAGE_SIZE * 4)
        viewModel.loadMore()
        assert(result.getSuccessData()!!.size == PAGE_SIZE * 5)
        viewModel.loadMore()
        assert(result.getSuccessData()!!.size == 101)
        assert(result.end())
        viewModel.loadMore()
        assert(result.getError() is NoMoreData)
    }
}