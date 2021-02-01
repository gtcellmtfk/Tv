package com.bytebyte6.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.data.AppDatabase
import com.bytebyte6.data.dataModule
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.PlaylistTvCrossRef
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
class DeletePlaylistUseCaseTest : KoinTest {

    private val db: AppDatabase by inject()
    private val deletePlaylistUseCase: DeletePlaylistUseCase by inject()

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
        val playlistId = db.playlistDao().insert(Playlist(playlistName = "A1"))
        val tvs = mutableListOf<Tv>().apply {
            add(Tv(name = "A",url = "A"))
            add(Tv(name = "B",url = "B"))
        }
        val ids=db.tvDao().insert(tvs)
        assert(ids.size == 2)
        assert(db.tvDao().getCount() == 2)
        val newTvs = db.tvDao().getTvs()
        val p1 = PlaylistTvCrossRef(playlistId, newTvs[0].tvId)
        val p2 = PlaylistTvCrossRef(playlistId, newTvs[1].tvId)
        db.playlistTvCrossRefDao().insert(mutableListOf(p1, p2))
        val playlistWithTvsById = db.playlistDao().getPlaylistWithTvsById(playlistId)
        assert(playlistWithTvsById.tvs.size == 2)
        deletePlaylistUseCase.execute(mutableListOf(playlistWithTvsById.playlist)).test()
            .assertValue {
                true
            }
        assert(db.playlistDao().getPlaylists().isEmpty())
    }
}