package com.bytebyte6.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.data.entity.Tv
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.java.KoinJavaComponent.inject
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TvDaoTest : KoinTest {

    private lateinit var db: AppDatabase
    private lateinit var tvDao: TvDao
    private lateinit var context: Context
    private val list = mutableListOf<Tv>().apply {
        add(Tv(url = "https://y5w8j4a9.ssl.hwcdn.net/andprivehd/tracks-v1a1/a.m3u8", name = "A"))
        add(Tv(url = "https://y5w8j4a9.ssl.hwcdn.net/andprivehd/tracks-v1a1/b.m3u8", name = "B"))
        add(Tv(url = "https://y5w8j4a9.ssl.hwcdn.net/andprivehd/tracks-v1a1/c.m3u8", name = "C"))
    }

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(dataModule)
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val converter by inject(Converter::class.java)

    @Before
    fun createDb() {
        context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        tvDao = db.tvDao()
        tvDao.insertAll(list)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testReplaceSameUrl() {
        val allForTest1 = tvDao.get()
        val tv =
            Tv(url = "https://y5w8j4a9.ssl.hwcdn.net/andprivehd/tracks-v1a1/a.m3u8", name = "D")
        val list = mutableListOf(tv)
        tvDao.insertAll(list)
        val allForTest2 = tvDao.get()
        assert(allForTest2 != allForTest1)
    }

    @Test
    @Throws(Exception::class)
    fun testReplaceUrl() {
        var allForTest = tvDao.get()
        allForTest[0].url = "https://y5w8j4a9.ssl.hwcdn.net/andprivehd/tracks-v1a1/d.m3u8"
        tvDao.insertAll(allForTest)
        allForTest = tvDao.get()
        assert(allForTest[0].url == "https://y5w8j4a9.ssl.hwcdn.net/andprivehd/tracks-v1a1/d.m3u8")
    }

    @Test
    @Throws(Exception::class)
    fun testReplaceName() {
        var allForTest = tvDao.get()
        allForTest[0].name = "D"
        tvDao.insertAll(allForTest)
        allForTest = tvDao.get()
        assert(allForTest[0].name == "D")
    }

    @Test
    @Throws(Exception::class)
    fun testInsertAll() {
        assert(tvDao.count() == list.size)
    }
}