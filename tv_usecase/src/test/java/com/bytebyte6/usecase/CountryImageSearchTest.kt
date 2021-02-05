package com.bytebyte6.usecase

import com.bytebyte6.testdata.countries
import com.bytebyte6.data.entity.Country
import com.bytebyte6.image.SearchImageImpl
import com.bytebyte6.usecase.work.CountryImageSearch
import org.junit.Test

class CountryImageSearchTest {
    @Test
    fun test() {
        val dataManager = object : com.bytebyte6.testdata.TestDataManager() {
            override fun getCountries(): List<Country> {
                return countries.apply {
                    //测试过滤
                    this[0].image = "aa"
                }
            }

            val ups = mutableListOf<Country>()

            override fun updateCountry(countries: List<Country>) {
                ups.addAll(countries)
            }
        }
        val countryImageSearch = CountryImageSearch(dataManager, SearchImageImpl())
        countryImageSearch.doThatShit()
        assert(dataManager.ups.size == countries.size - 1)
        dataManager.ups.forEach {
            assert(it.image.isNotEmpty())
        }
    }
}