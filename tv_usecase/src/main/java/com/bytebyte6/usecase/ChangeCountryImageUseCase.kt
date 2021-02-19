package com.bytebyte6.usecase

import androidx.annotation.Keep
import com.bytebyte6.common.RxUseCase
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Country
import com.bytebyte6.image.SearchImage
import kotlin.random.Random

class ChangeCountryImageUseCase(
    private val searchImage: SearchImage,
    private val dataManager: DataManager
) : RxUseCase<CountryParam, CountryParam>() {
    private val flags = mutableListOf(" Flag", " flag", " 旗帜")
    private fun randomFlag(countryName: String) =
        countryName.plus(flags[Random.Default.nextInt(3)])

    override fun run(param: CountryParam): CountryParam {
        val country = param.country
        val result = searchImage.search(randomFlag(country.name))
        if (result.isNotEmpty()) {
            country.image = result
            dataManager.updateCountry(country)
        }
        return param
    }
}

@Keep
data class CountryParam(
    val country: Country,val pos:Int
)