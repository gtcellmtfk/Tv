package com.bytebyte6.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.data.dao.*
import com.bytebyte6.data.entity.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import java.io.IOException

/**
 * 有用测试样例
 * 多对多关系测试
 */
@RunWith(AndroidJUnit4::class)
class CrossRefTest : KoinTest {

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var playlistDao: PlaylistDao
    private lateinit var tvDao: TvDao
    private lateinit var playlistTvCrossRefDao: PlaylistTvCrossRefDao
    private lateinit var userPlaylistCrossRefDao: UserPlaylistCrossRefDao
    private lateinit var context: Context
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
    val koinTestRule = KoinTestRule.create {
        modules(dataModule)
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

        userDao = db.userDao()
        playlistDao = db.playlistDao()
        tvDao = db.tvDao()
        userPlaylistCrossRefDao = db.userPlaylistCrossRefDao()
        playlistTvCrossRefDao = db.playlistTvCrossRefDao()

        tvDao.insert(tvs)
        userDao.insert(users)
        playlistDao.insert(playlists)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun tvOneToMany() {
        val playlists = playlistDao.getPlaylists()
        val tvs = tvDao.getTvs()

        val a = PlaylistTvCrossRef(playlists[0].playlistId, tvs[0].tvId)
        val b = PlaylistTvCrossRef(playlists[1].playlistId, tvs[0].tvId)
        val c = PlaylistTvCrossRef(playlists[2].playlistId, tvs[0].tvId)

        playlistTvCrossRefDao.insert(mutableListOf(a, b, c))

        val list = tvDao.getTvWithPlaylistss()
        assert(list[0].tv.tvId == tvs[0].tvId)
        assert(list[0].playlists.size == 3)
    }

    @Test
    @Throws(Exception::class)
    fun userOneToMany() {
        val playlists = playlistDao.getPlaylists()
        val users = userDao.getUsers()

        val a = UserPlaylistCrossRef(users[0].userId, playlists[0].playlistId)
        val b = UserPlaylistCrossRef(users[0].userId, playlists[1].playlistId)
        val c = UserPlaylistCrossRef(users[0].userId, playlists[2].playlistId)

        userPlaylistCrossRefDao.insert(mutableListOf(a, b, c))

        userDao.getUsersWithPlaylists().apply {
            assert(this[0].user.userId == users[0].userId)
            assert(this[0].playlists.size == 3)
        }
    }

    @Test
    @Throws(Exception::class)
    fun playlistOneToMany() {
        val tvs = tvDao.getTvs()
        val playlists = playlistDao.getPlaylists()
        val users = userDao.getUsers()

        val ref1 = PlaylistTvCrossRef(playlists[0].playlistId, tvs[0].tvId)
        val ref2 = PlaylistTvCrossRef(playlists[0].playlistId, tvs[1].tvId)
        val ref3 = PlaylistTvCrossRef(playlists[0].playlistId, tvs[2].tvId)

        playlistTvCrossRefDao.insert(mutableListOf(ref1, ref2, ref3))

        val a = UserPlaylistCrossRef(users[0].userId, playlists[0].playlistId)
        val b = UserPlaylistCrossRef(users[1].userId, playlists[0].playlistId)
        val c = UserPlaylistCrossRef(users[2].userId, playlists[0].playlistId)

        userPlaylistCrossRefDao.insert(mutableListOf(a, b, c))

        val playlistsWithTvs = playlistDao.getPlaylistsWithTvss()
        assert(playlistsWithTvs[0].playlist.playlistId == playlists[0].playlistId)
        assert(playlistsWithTvs[0].tvs.size == 3)

        val playlistsWithUsers = playlistDao.getPlaylistsWithUsers()
        assert(playlistsWithUsers[0].playlist.playlistId == playlists[0].playlistId)
        assert(playlistsWithUsers[0].users.size == 3)

        printList(playlistsWithTvs)
        printList(playlistsWithUsers)
    }

    private fun printList(list: List<Any>) {
        list.forEach {
            println(it)
        }
    }
}