package com.bytebyte6.usecase

import androidx.annotation.Keep
import com.bytebyte6.common.RxUseCase
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Country
import com.bytebyte6.image.SearchImage
import kotlin.random.Random

object IllegalParamException : IllegalArgumentException("Illegal param!!!")

class SearchCountryImageUseCase(
    private val searchImage: SearchImage,
    private val dataManager: DataManager
) : RxUseCase<SearchCountryImageParam, SearchCountryImageParam>() {
    private val flags = mutableListOf(" Flag", " flag", "国旗")

    private fun randomFlag(countryName: String) =
        countryName.plus(flags[Random.Default.nextInt(3)])

    override fun run(param: SearchCountryImageParam): SearchCountryImageParam {
        if (param.logoWrong) {
            val wrongPos = param.first
            val country = param.cs[wrongPos]
            val result = searchImage.search(randomFlag(country.name))
            dataManager.updateCountry(country.apply { image = result })
        } else {
            val first = param.first
            val last = param.last
            if (first == 0 && last == 0) throw IllegalParamException
            if (first > last) throw IllegalParamException
            if (last >= param.cs.size) throw IllegalParamException
            val cs2 = param.cs.subList(first, last + 1)
            cs2.forEach {
                if (it.image.isEmpty() && it.name.isNotEmpty()) {
                    val search = searchImage.search(randomFlag(it.name))
                    if (search.isNotEmpty()) {
                        it.image = search
                        dataManager.updateCountry(it)
                    }
                }
            }
        }
        return param
    }
}

@Keep
data class SearchCountryImageParam(
    val first: Int,
    val last: Int,
    var cs: List<Country>,
    val logoWrong: Boolean = false
)