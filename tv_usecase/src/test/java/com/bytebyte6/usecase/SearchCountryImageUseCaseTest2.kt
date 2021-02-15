package com.bytebyte6.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bytebyte6.image.SearchImageImpl
import com.bytebyte6.testdata.TestDataManager
import com.bytebyte6.testdata.countries
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchCountryImageUseCaseTest2 {

    private lateinit var searchCountryImageUseCase: SearchCountryImageUseCase
    private lateinit var dataManager: TestDataManager

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        dataManager = object : TestDataManager() {}
        searchCountryImageUseCase = SearchCountryImageUseCase(SearchImageImpl(), dataManager)
    }

    @Test
    fun test() {
        val param = SearchCountryImageParam(
            0, 0, emptyList()
        )
        searchCountryImageUseCase.execute(
            param
        ).test().assertError(IllegalParamException)

        val param1 = SearchCountryImageParam(
            1, 0, emptyList()
        )
        searchCountryImageUseCase.execute(
            param1
        ).test().assertError(IllegalParamException)

        val param2 = SearchCountryImageParam(
            0, 11, countries
        )
        searchCountryImageUseCase.execute(
            param2
        ).test().assertError(IllegalParamException)

        val param3 = SearchCountryImageParam(
            0, 2, countries
        )
        searchCountryImageUseCase.execute(param3).test().assertValue {
            assert(it.cs[0].image.isNotEmpty())
            assert(it.cs[1].image.isNotEmpty())
            assert(it.cs[2].image.isNotEmpty())
            true
        }

        assert(dataManager.testCountries.size == 3)
        assert(dataManager.testCountries[0].image.isNotEmpty())
        assert(dataManager.testCountries[1].image.isNotEmpty())
        assert(dataManager.testCountries[2].image.isNotEmpty())
    }
}