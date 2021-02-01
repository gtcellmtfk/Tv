package com.bytebyte6.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.data.AppDatabase
import com.bytebyte6.data.dao.CountryDao
import com.bytebyte6.data.dataModule
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.roomMemoryModule
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
class CountryImageSearchUseCaseTest : KoinTest {

    private val db: AppDatabase by inject()
    private val countryDao: CountryDao by inject()
    private val countryImageSearchUseCase: CountryImageSearchUseCase by inject()

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

    /**
     * 测试images不为空的情况 = false
     */
    @Test
    fun test_countryImageEmpty() {
        val china = Country(image = ("https//:www.007.com/1.png"))
        val id = countryDao.insert(china)
        val newChina = Country(id, china.name, image = china.image)
        countryImageSearchUseCase.execute(newChina).test().assertValue(false)
    }


    /**
     * 测试名称为空的情况 = false
     */
    @Test
    fun test_countryNameEmpty() {
        val china = Country(name = "")
        val id = countryDao.insert(china)
        val newChina = Country(id, china.name, image = china.image)
        countryImageSearchUseCase.execute(newChina).test().assertValue(false)
    }

    /**
     * 测试名称不为空且Image为空的情况 = true
     */
    @Test
    fun test_countryNameNotEmpty() {
        val china = Country(name = "CHINA")
        val id = countryDao.insert(china)
        val newChina = Country(id, china.name, image = china.image)
        countryImageSearchUseCase.execute(newChina).test().assertValue(true)
    }
}