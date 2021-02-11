package com.bytebyte6.usecase.work

import com.bytebyte6.data.DataManager
import com.bytebyte6.image.SearchImage

class SearchCountryImage(
    private val dataManager: DataManager,
    private val searchImage: SearchImage
) {
    fun searchCountryImage() {
        val countries = dataManager.getImageEmptyCountries()
        countries.forEach { country ->
            val image = searchImage.search(country.name.plus("+flag"))
            if (image.isNotEmpty()) {
                country.image = image
                dataManager.updateCountry(country)
            }
        }
    }
}