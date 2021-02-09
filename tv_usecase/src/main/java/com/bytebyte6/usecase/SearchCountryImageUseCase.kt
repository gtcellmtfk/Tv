package com.bytebyte6.usecase

import androidx.annotation.Keep
import com.bytebyte6.common.RxUseCase
import com.bytebyte6.common.logd
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Country
import com.bytebyte6.image.SearchImage

class SearchCountryImageUseCase(
    private val searchImage: SearchImage,
    private val dataManager: DataManager
) : RxUseCase<SearchCountryImageParam, Boolean>() {
    override fun run(param: SearchCountryImageParam): Boolean {
        val first = param.first
        val last = param.last
        if (first == 0 && last == 0) return false
        if (first > last) return false
        if (last >= param.cs.size) return false
        val cs2 = param.cs.subList(first, last + 1)
        cs2.forEach {
            if (it.image.isEmpty() && it.name.isNotEmpty()) {
                val search = searchImage.search(it.name.plus("+flag"))
                if (search.isNotEmpty()) {
                    it.image = search
                    dataManager.updateCountry(it)
                }
            }
        }
        return true
    }
}

@Keep
data class SearchCountryImageParam(
    val first: Int,
    val last: Int,
    val cs: List<Country>
)