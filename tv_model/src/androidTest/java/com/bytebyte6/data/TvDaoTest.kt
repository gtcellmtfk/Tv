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
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TvDaoTest  {

    private lateinit var db: AppDatabase
    private lateinit var tvDao: TvDao
    private lateinit var context: Context

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        tvDao = db.tvDao()
        tvDao.insert(tvList)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testDeleteNotExist() {
        val tv = Tv(url = "6666666666666666666666666666")
        tvDao.delete(tv)
    }

    @Test
    fun testInsertExist() {
        val tv = Tv(url = "https://y5w8j4a09.ssl.hwcdn.net/andprivehd/tracks-v1a1/a.m3u8")
        val id = tvDao.insert(tv)
        assert(id != 0L)
    }

    @Test
    fun testGetTvByUrl() {
        val url = "https://y5w8j4a09.ssl.hwcdn.net/andprivehd/tracks-v1a1/a1.m3u8"
        val tv = tvDao.getTvByUrl(url)
        assert(tv == null)
    }

    @Test
    fun testPaging() {
        val count = tvDao.getCount()
        assert(count != 0)

        val all = mutableListOf<Tv>()

        val temp = count % PAGE_SIZE
        val page = count.div(PAGE_SIZE).plus(if (temp == 0) 0 else 1)

        for (i in 0 until page) {
            all.addAll(tvDao.paging(i * PAGE_SIZE))
        }

        assert(all.toSet().size == tvList.size)
    }

    @Test
    @Throws(Exception::class)
    fun testReplaceSameUrl() {
        val allForTest1 = tvDao.getTvs()
        val tv =
            Tv(url = "https://y5w8j4a9.ssl.hwcdn.net/andprivehd/tracks-v1a1/a.m3u8", name = "D")
        val list = mutableListOf(tv)
        tvDao.insert(list)
        val allForTest2 = tvDao.getTvs()
        assert(allForTest2 != allForTest1)
    }

    @Test
    @Throws(Exception::class)
    fun testReplaceUrl() {
        var allForTest = tvDao.getTvs()
        allForTest[0].url = "https://y5w8j4a9.ssl.hwcdn.net/andprivehd/tracks-v1a1/d.m3u8"
        tvDao.insert(allForTest)
        allForTest = tvDao.getTvs()
        assert(allForTest[0].url != "https://y5w8j4a9.ssl.hwcdn.net/andprivehd/tracks-v1a1/d.m3u8")
    }

    @Test
    @Throws(Exception::class)
    fun testReplaceName() {
        var allForTest = tvDao.getTvs()
        allForTest[0].name = "D"
        tvDao.insert(allForTest)
        allForTest = tvDao.getTvs()
        assert(allForTest[0].name != "D")
    }

    @Test
    @Throws(Exception::class)
    fun testInsertAll() {
        assert(tvDao.getCount() == tvList.size)
    }
}