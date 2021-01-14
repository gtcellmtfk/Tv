package com.bytebyte6.data

import android.content.Context
import android.util.ArrayMap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.data.dao.TvFtsDao
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.TvFts
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class TvFtsDaoTest : KoinTest {

    private lateinit var db: AppDatabase
    private lateinit var tvFtsDao: TvFtsDao
    private lateinit var tvDao: TvDao
    private lateinit var context: Context

    private lateinit var map: ArrayMap<String, Int>

    private lateinit var countries: List<String>

    private lateinit var tvList: List<Tv>

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
        tvFtsDao = db.tvFtsDao()
        tvDao = db.tvDao()
        map = ArrayMap<String, Int>().apply {
            put("CHINA", 0)
            put("US", 0)
            put("C", 0)
        }
        countries = mutableListOf(
            "CHINA", "US", "C"
        )
        tvList = mutableListOf<Tv>().apply {
            for (i in 0 until FAKE_SIZE) {
                val countryName = countries[Random.Default.nextInt(3)]
                val count = map[countryName]?.plus(1)
                map[countryName] = count
                add(
                    Tv(
                        url = "https://y5w8j4a${i}9.ssl.hwcdn.net/andprivehd/tracks-v1a1/a.m3u8",
                        name = "A$i",
                        countryName = countryName
                    )
                )
            }
        }
        tvDao.insert(tvList)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun test_getCount() {
        assert(map["CHINA"] == tvFtsDao.getCount("CHINA"))
        assert(map["US"] == tvFtsDao.getCount("US"))
        assert(map["C"] == tvFtsDao.getCount("C"))
    }

    @Test
    fun test_paging() {
        val count = tvFtsDao.getCount("CHINA")
        val page = count.div(PAGE_SIZE).plus(
            if (count % PAGE_SIZE == 0) 0 else 1
        )
        val list = mutableListOf<TvFts>()
        for (i in 0 until page) {
            list.addAll(
                tvFtsDao.paging(i * PAGE_SIZE, "CHINA")
            )
        }
        assert(list.size == count)
        assert(list.size == map["CHINA"])
        assert(count == map["CHINA"])
    }
}