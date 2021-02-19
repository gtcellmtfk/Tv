package com.bytebyte6.usecase.work

import com.bytebyte6.common.logd
import com.bytebyte6.data.DataManager
import com.bytebyte6.image.SearchImage
import kotlin.random.Random

class SearchCountryImage(
    private val dataManager: DataManager,
    private val searchImage: SearchImage
) {
    private val flags = mutableListOf(" Flag", " flag", " 旗帜")
    private fun randomFlag(countryName: String) =
        countryName.plus(flags[Random.Default.nextInt(3)])
    fun searchCountryImage() {
        val countries = dataManager.getImageEmptyCountries()
        countries.forEach { country ->
            val image = searchImage.search(randomFlag(country.name))
            logd("image=$image")
            if (image.isNotEmpty()) {
                country.image = image
                dataManager.updateCountry(country)
            }
        }
    }
}