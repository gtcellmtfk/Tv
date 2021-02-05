package com.bytebyte6.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bytebyte6.data.entity.Country
import com.bytebyte6.image.SearchImageImpl
import com.bytebyte6.testdata.TestDataManager
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CountryImageSearchUseCaseTest2 {

    private lateinit var countryImageSearchUseCase: CountryImageSearchUseCase
    private lateinit var dataManager: TestDataManager

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        dataManager = object : TestDataManager() {}
        countryImageSearchUseCase = CountryImageSearchUseCase(SearchImageImpl(), dataManager)
    }

    /**
     * 测试images不为空的情况 = false
     */
    @Test
    fun test_countryImageEmpty() {
        val china = Country(image = ("https//:www.007.com/1.png"))
        countryImageSearchUseCase.execute(china).test().assertValue(false)
    }


    /**
     * 测试名称为空的情况 = false
     */
    @Test
    fun test_countryNameEmpty() {
        val china = Country(name = "", image = "")
        countryImageSearchUseCase.execute(china).test().assertValue(false)
    }

    /**
     * 测试名称不为空且Image为空的情况 = true
     */
    @Test
    fun test_countryNameNotEmpty() {
        val china = Country(name = "CHINA", image = "")
        countryImageSearchUseCase.execute(china).test().assertValue(true)
    }
}