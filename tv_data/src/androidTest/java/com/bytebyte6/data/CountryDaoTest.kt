package com.bytebyte6.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.data.dao.CountryDao
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.data.work.CountryImageSearch
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTestRule

@RunWith(AndroidJUnit4::class)
class CountryDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var tvDao: TvDao
    private lateinit var countryDao: CountryDao
    private lateinit var context: Context

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(dataModule)
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val converter = Converter()
        context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        tvDao = db.tvDao()
        countryDao = db.countryDao()

        val tvs = converter.getTvs(context)

        val cs = tvs.map {
            it.country
        }

        val defaultImageSearch= CountryImageSearch(cs)

        countryDao.insert(defaultImageSearch.doThatShit())

        tvs.forEach {
            val countryName = it.country.name
            if (countryName.isNotEmpty()) {
                it.countryId = countryDao.getIdByName(countryName)
            }
        }

        tvDao.insert(tvs)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun test() {
        val list = countryDao.getCountryWithTvs()
        list.forEach { countryWithTvs ->
            println("country = ${countryWithTvs.country.name} tv count = ${countryWithTvs.tvs.size}")
            countryWithTvs.tvs.forEach {
                assert(countryWithTvs.country.countryId==it.countryId)
            }
        }
    }
}