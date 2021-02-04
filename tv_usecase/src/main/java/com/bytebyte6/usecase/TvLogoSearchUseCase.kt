package com.bytebyte6.usecase

import androidx.annotation.Keep
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.common.Result
import com.bytebyte6.common.RxUseCase2
import com.bytebyte6.data.DataManager
import com.bytebyte6.image.SearchImage

interface TvLogoSearchUseCase : RxUseCase2<SearchParam, SearchParam>

class TvLogoSearchUseCaseImpl(
    private val imageSearch: SearchImage,
    private val dataManager: DataManager
) : TvLogoSearchUseCase {

    override val result: MutableLiveData<Result<SearchParam>> = MutableLiveData()

    override fun run(param: SearchParam): SearchParam {
        val tv = dataManager.getTvById(param.id)
        if (tv.logo.isEmpty()) {
            val logo = imageSearch.search(tv.name)
            if (logo.isNotEmpty()) {
                tv.logo = logo
                dataManager.updateTv(tv)
                param.logo = logo
            }
        } else {
            param.apply {
                logo = tv.logo
            }
        }
        return param
    }
}

@Keep
data class SearchParam(
    var id: Long,
    var pos: Int,
    var logo: String = ""
)