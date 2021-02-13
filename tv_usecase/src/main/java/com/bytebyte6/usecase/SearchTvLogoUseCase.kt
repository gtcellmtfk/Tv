package com.bytebyte6.usecase

import androidx.annotation.Keep
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.common.Result
import com.bytebyte6.common.RxUseCase2
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.image.SearchImage

interface SearchTvLogoUseCase : RxUseCase2<SearchTvLogoParam, SearchTvLogoParam>

class SearchTvLogoUseCaseImpl(
    private val searchImage: SearchImage,
    private val dataManager: DataManager
) : SearchTvLogoUseCase {
    override val result: MutableLiveData<Result<SearchTvLogoParam>> = MutableLiveData()

    override fun run(param: SearchTvLogoParam): SearchTvLogoParam {
        param.tvs.filter {
            it.logo.isEmpty() && it.name.isNotEmpty()
        }.forEach {
            val result = searchImage.search(it.name)
            if (result.isNotEmpty()) {
                it.logo = result
                dataManager.updateTv(it)
            }
        }
        return param
    }
}

@Keep
data class SearchTvLogoParam(
    var tvs: List<Tv>
)