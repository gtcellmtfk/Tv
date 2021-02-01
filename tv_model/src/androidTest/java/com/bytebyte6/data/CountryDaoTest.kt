package com.bytebyte6.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.data.dao.CountryDao
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.entity.Tv
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule

/**
 * 无用测试样例
 */
@RunWith(AndroidJUnit4::class)
class CountryDaoTest : KoinTest {

    companion object {
        const val CHINA = "China"
        const val US = "US"
    }

    private lateinit var db: AppDatabase
    private lateinit var countryDao: CountryDao
    private lateinit var context: Context
    private val china = Country(name = CHINA)
    private val us = Country(name = US)

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
        countryDao = db.countryDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun test_getCount() {
        assert(countryDao.getCount() == 0)
        countryDao.insert(china)
        assert(countryDao.getCount() == 1)
    }

    @Test
    fun test_getIdByName() {
        countryDao.insert(china)
        countryDao.getIdByName(CHINA)
    }

    @Test
    fun test_getCountries() {
        countryDao.insert(china)
        assert(countryDao.getCountries().size == 1)
        assert(countryDao.getCountries()[0].name == CHINA)
    }

    @Test
    fun test_getCountryWithTvss() {
        val ids = countryDao.insert(mutableListOf(china, us))
        val tvDao = db.tvDao()
        val tv1 = Tv(url = "www.baidu1.com", countryId = ids[1])
        val tv2 = Tv(url = "www.baidu2.com", countryId = ids[0])
        val tv3 = Tv(url = "www.baidu3.com", countryId = ids[0])
        tvDao.insert(mutableListOf(tv1, tv2, tv3))
        val countryWithTvs = countryDao.getCountryWithTvss()
        countryWithTvs.forEach {
            if (it.country.countryId == ids[0]) {
                assert(it.country.name == CHINA)
                assert(it.tvs.size == 2)
            } else if (it.country.countryId == ids[1]) {
                assert(it.country.name == US)
                assert(it.tvs.size == 1)
                assert(it.tvs[0].url==tv1.url)
            }
        }
    }

    @Test
    fun test_getCountryWithTvs() {
        val ids = countryDao.insert(mutableListOf(china, us))
        val tvDao = db.tvDao()
        val tv1 = Tv(url = "www.baidu1.com", countryId = ids[1])
        val tv2 = Tv(url = "www.baidu2.com", countryId = ids[0])
        val tv3 = Tv(url = "www.baidu3.com", countryId = ids[0])
        tvDao.insert(mutableListOf(tv1, tv2, tv3))
        val countryWithTvs = countryDao.getCountryWithTvs(ids[0])
        assert(countryWithTvs.country.name == CHINA)
        assert(countryWithTvs.tvs.size == 2)
        val countryWithTvsUs = countryDao.getCountryWithTvs(ids[1])
        assert(countryWithTvsUs.country.name == US)
        assert(countryWithTvsUs.tvs.size == 1)
        assert(countryWithTvsUs.tvs[0].url==tv1.url)
    }

    @Test
    fun test_countryWithTvs() {
        val ids = countryDao.insert(mutableListOf(china, us))
        val tvDao = db.tvDao()
        val tv1 = Tv(url = "www.baidu1.com", countryId = ids[1])
        val tv2 = Tv(url = "www.baidu2.com", countryId = ids[0])
        val tv3 = Tv(url = "www.baidu3.com", countryId = ids[0])
        tvDao.insert(mutableListOf(tv1, tv2, tv3))
        val countryWithTvs = countryDao.countryWithTvs(ids[0])
        countryWithTvs.observeForever {
            assert(it.country.name == CHINA)
            assert(it.tvs.size == 2)
        }
        val countryWithTvsUs = countryDao.countryWithTvs(ids[1])
        countryWithTvsUs.observeForever {
            assert(it.country.name == US)
            assert(it.tvs.size == 1)
            assert(it.tvs[0].url==tv1.url)
        }
    }

    @Test
    fun test_countries() {
        countryDao.insert(china)
        countryDao.countries().observeForever {
            assert(it.size == 1)
            assert(it[0].name == CHINA)
        }
    }
}