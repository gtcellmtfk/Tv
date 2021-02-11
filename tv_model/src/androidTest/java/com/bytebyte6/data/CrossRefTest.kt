package com.bytebyte6.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.lib_test.assertError
import com.bytebyte6.data.entity.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 多对多关系测试
 */
@RunWith(AndroidJUnit4::class)
class CrossRefTest {

    private lateinit var dataManager: DataManager
    private lateinit var db: AppDatabase

    private val users = mutableListOf<User>().apply {
        add(User(name = "A 1"))
        add(User(name = "A 2"))
        add(User(name = "A 3"))
    }
    private val tvs = mutableListOf<Tv>().apply {
        add(Tv(url = "https://y5w8j4a9.ssl.hwcdn.net/andprivehd/tracks-v1a1/a.m3u8", name = "A"))
        add(Tv(url = "https://y5w8j4a9.ssl.hwcdn.net/andprivehd/tracks-v1a1/b.m3u8", name = "B"))
        add(Tv(url = "https://y5w8j4a9.ssl.hwcdn.net/andprivehd/tracks-v1a1/c.m3u8", name = "C"))
    }
    private val playlists = mutableListOf<Playlist>().apply {
        add(Playlist(playlistName = "PLAY 1"))
        add(Playlist(playlistName = "PLAY 2"))
        add(Playlist(playlistName = "PLAY 3"))
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dataManager = DataManagerImpl(db)

        dataManager.insertTv(tvs)
        dataManager.insertUser(users)
        dataManager.insertPlaylist(playlists)
    }

    @After
    fun close() {
        db.close()
    }

    @Test
    fun tvOneToMany() {
        val playlists = dataManager.getPlaylists()
        val tvs = dataManager.getTvs()

        val a = PlaylistTvCrossRef(playlists[0].playlistId, tvs[0].tvId)
        val b = PlaylistTvCrossRef(playlists[1].playlistId, tvs[0].tvId)
        val c = PlaylistTvCrossRef(playlists[2].playlistId, tvs[0].tvId)

        dataManager.crossRefPlaylistWithTv(mutableListOf(a, b, c))

        val list = dataManager.getTvWithPlaylistss()
        assert(list[0].tv.tvId == tvs[0].tvId)
        assert(list[0].playlists.size == 3)
    }

    @Test
    fun userOneToMany() {
        val playlists = dataManager.getPlaylists()
        val users = dataManager.getUsers()

        val a = UserPlaylistCrossRef(users[0].userId, playlists[0].playlistId)
        val b = UserPlaylistCrossRef(users[0].userId, playlists[1].playlistId)
        val c = UserPlaylistCrossRef(users[0].userId, playlists[2].playlistId)

        dataManager.crossRefUserWithPlaylists(mutableListOf(a, b, c))

        dataManager.getUserWithPlaylists().apply {
            assert(this[0].user.userId == users[0].userId)
            assert(this[0].playlists.size == 3)
        }
    }

    @Test
    fun playlistOneToMany() {
        val tvs = dataManager.getTvs()
        val playlists = dataManager.getPlaylists()
        val users = dataManager.getUsers()

        val ref1 = PlaylistTvCrossRef(playlists[0].playlistId, tvs[0].tvId)
        val ref2 = PlaylistTvCrossRef(playlists[0].playlistId, tvs[1].tvId)
        val ref3 = PlaylistTvCrossRef(playlists[0].playlistId, tvs[2].tvId)

        dataManager.crossRefPlaylistWithTv(mutableListOf(ref1, ref2, ref3))

        val a = UserPlaylistCrossRef(users[0].userId, playlists[0].playlistId)
        val b = UserPlaylistCrossRef(users[1].userId, playlists[0].playlistId)
        val c = UserPlaylistCrossRef(users[2].userId, playlists[0].playlistId)

        dataManager.crossRefUserWithPlaylists(mutableListOf(a, b, c))

        val playlistsWithTvs = dataManager.getPlaylistsWithTvss()
        assert(playlistsWithTvs[0].playlist.playlistId == playlists[0].playlistId)
        assert(playlistsWithTvs[0].tvs.size == 3)

        val playlistsWithUsers = dataManager.getPlaylistsWithUsers()
        assert(playlistsWithUsers[0].playlist.playlistId == playlists[0].playlistId)
        assert(playlistsWithUsers[0].users.size == 3)
    }

    @Test
    fun testPlaylistWithTvsCache() {
        playlistOneToMany()

        val playlists = dataManager.getPlaylists()
        assert(playlists.size == 3)

        val playlistId = playlists[0].playlistId
        val tvsByPlaylistId = dataManager.getTvsByPlaylistId(playlistId, 0)
        assert(tvsByPlaylistId.size == 3)

        assertError {
            dataManager.getTvsByPlaylistId(playlistId, 1)
        }

        tvsByPlaylistId[0].name = "996"
        dataManager.updatePlaylistCache(playlistId, tvsByPlaylistId, 0)
        assert(dataManager.getTvsByPlaylistId(playlistId, 0)[0].name == "996")

        assertError {
            dataManager.updatePlaylistCache(playlistId, emptyList(), 0)
        }

        assertError {
            dataManager.updatePlaylistCache(playlistId, tvsByPlaylistId, 1)
        }

        assert(dataManager.getTvCountByPlaylistId(playlistId) == 3)
    }
}