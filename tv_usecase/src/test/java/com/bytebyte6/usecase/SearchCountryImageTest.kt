package com.bytebyte6.usecase

import com.bytebyte6.testdata.countries
import com.bytebyte6.data.entity.Country
import com.bytebyte6.image.SearchImageImpl
import com.bytebyte6.usecase.work.SearchCountryImage
import org.junit.Test

class SearchCountryImageTest {
    @Test
    fun test() {
        val dataManager = object : com.bytebyte6.testdata.TestDataManager() {
            override fun getCountries(): List<Country> {
                return countries
            }

            val ups = mutableListOf<Country>()

            override fun updateCountry(countries: List<Country>) {
                ups.addAll(countries)
            }
        }
        val countryImageSearch = SearchCountryImage(dataManager, SearchImageImpl())
        countryImageSearch.searchCountryImage()
        dataManager.ups.forEach {
            assert(it.image.isNotEmpty())
        }
    }
}