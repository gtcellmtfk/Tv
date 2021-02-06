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
        searchCountryImageUseCase.execute(
            SearchCountryImageParam(
                0, 0, emptyList()
            )
        ).test().assertValue(false)

        searchCountryImageUseCase.execute(
            SearchCountryImageParam(
                1, 0, emptyList()
            )
        ).test().assertError(IllegalArgumentException::class.java)

        searchCountryImageUseCase.execute(
            SearchCountryImageParam(
                0, 11, countries
            )
        ).test().assertError(IllegalStateException::class.java)

        searchCountryImageUseCase.execute(
            SearchCountryImageParam(
                0, 2, countries
            )
        ).test().assertValue(true)

        assert(dataManager.testCountries.size == 3)
        assert(dataManager.testCountries[0].image.isNotEmpty())
        assert(dataManager.testCountries[1].image.isNotEmpty())
        assert(dataManager.testCountries[2].image.isNotEmpty())
    }
}