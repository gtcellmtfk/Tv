package com.bytebyte6.usecase

import androidx.annotation.Keep
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.common.Result
import com.bytebyte6.common.RxUseCase2
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.image.SearchImage

interface SearchTvLogoUseCase : RxUseCase2<SearchTvLogoParam, SearchTvLogoParam> {
    val itemChanged: MutableLiveData<Tv>
    fun stop()
}

class SearchTvLogoUseCaseImpl(
    private val searchImage: SearchImage,
    private val dataManager: DataManager
) : SearchTvLogoUseCase {

    private var stop = false

    override val itemChanged: MutableLiveData<Tv> = MutableLiveData()

    override val result: MutableLiveData<Result<SearchTvLogoParam>> = MutableLiveData()

    override fun run(param: SearchTvLogoParam): SearchTvLogoParam {
        param.tvs.filter {
            if (stop) {
                return param
            }
            it.logo.isEmpty()
        }.forEach {
            if (stop) {
                return param
            } else {
                val result = searchImage.search(it.name)
                if (result.isNotEmpty()) {
                    it.logo = result
                    dataManager.updateTv(it)
                    itemChanged.postValue(it)
                }
            }
        }
        return param
    }

    override fun stop() {
        stop = true
    }
}

@Keep
data class SearchTvLogoParam(
    var tvs: List<Tv>
)