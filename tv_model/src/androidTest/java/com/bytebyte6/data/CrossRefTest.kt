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

        // 数据准备
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
        // tv一对多关系验证
        // tv对playlist
        val playlists = dataManager.getPlaylists()
        val tvs = dataManager.getTvs()

        // tv和playlist 关联
        val a = PlaylistTvCrossRef(playlists[0].playlistId, tvs[0].tvId)
        val b = PlaylistTvCrossRef(playlists[1].playlistId, tvs[0].tvId)
        val c = PlaylistTvCrossRef(playlists[2].playlistId, tvs[0].tvId)

        // 插入关联的实体
        dataManager.crossRefPlaylistWithTv(mutableListOf(a, b, c))

        // 验证
        val list = dataManager.getTvWithPlaylistss()
        assert(list[0].tv.tvId == tvs[0].tvId)
        assert(list[0].playlists.size == 3)
    }

    @Test
    fun userOneToMany() {
        // user 对 playlist
        val playlists = dataManager.getPlaylists()
        val users = dataManager.getUsers()

        // 关联user 和 playlist
        val a = UserPlaylistCrossRef(users[0].userId, playlists[0].playlistId)
        val b = UserPlaylistCrossRef(users[0].userId, playlists[1].playlistId)
        val c = UserPlaylistCrossRef(users[0].userId, playlists[2].playlistId)

        // 插入关联
        dataManager.crossRefUserWithPlaylists(mutableListOf(a, b, c))

        // 验证
        dataManager.getUserWithPlaylists().apply {
            assert(this[0].user.userId == users[0].userId)
            assert(this[0].playlists.size == 3)
        }
    }

    @Test
    fun playlistOneToMany() {
        // 验证playlist 对 user 和 tv
        // 拥有这个playlist的用户
        // 存在这个playlist的tv
        // 一个tv可能存在于多个playlist
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

        // 获取所有的 playlist和tvs
        // 验证 一个playlist 对 多个tv
        val playlistsWithTvs = dataManager.getPlaylistsWithTvss()
        assert(playlistsWithTvs[0].playlist.playlistId == playlists[0].playlistId)
        assert(playlistsWithTvs[0].tvs.size == 3)

        // 获取所有的 playlist和users
        // 验证 一个playlist 对 多个用户
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
        val tvs = dataManager.getTvsByPlaylistId(playlistId, 0)
        assert(tvs.size == 3)

        assertError {
            dataManager.getTvsByPlaylistId(playlistId, 1)
        }

        tvs[0].name = "996"
        dataManager.updatePlaylistCache(playlistId, tvs, 0)
        assert(dataManager.getTvsByPlaylistId(playlistId, 0)[0].name == "996")

        assertError {
            dataManager.updatePlaylistCache(playlistId, emptyList(), 0)
        }

        assertError {
            dataManager.updatePlaylistCache(playlistId, tvs, 1)
        }

        assert(dataManager.getTvCountByPlaylistId(playlistId) == 3)
    }
}