package com.bytebyte6.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bytebyte6.testdata.TestDataManager
import com.bytebyte6.testdata.tvs
import com.google.gson.Gson
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InitAppUseCaseTest {

    private lateinit var dataManager: TestDataManager
    private lateinit var initAppUseCase: InitAppUseCaseImpl

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        dataManager = object : TestDataManager() {

        }
        initAppUseCase = InitAppUseCaseImpl(
            dataManager,
            null,
            Gson()
        )
    }

    @Test
    fun test() {
        initAppUseCase.setTvs(tvs)
        initAppUseCase.execute(Unit).test().assertValue(dataManager.testUsers[0])
        assert(dataManager.getTvCount() != 0)
        assert(dataManager.getCountryCount() != 0)
        assert(dataManager.getUsers().isNotEmpty())
        dataManager.testCountries.forEach {
            assert(it.countryId != 0L)
        }
        dataManager.testTvs.forEach {
            assert(it.language.isNotEmpty())
            assert(it.category.isNotEmpty())
        }
    }
}