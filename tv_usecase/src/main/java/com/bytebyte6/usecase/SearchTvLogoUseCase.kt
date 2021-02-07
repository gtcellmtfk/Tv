package com.bytebyte6.usecase

import androidx.annotation.Keep
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.common.Result
import com.bytebyte6.common.RxUseCase2
import com.bytebyte6.common.logd
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.image.SearchImage
import java.util.concurrent.atomic.AtomicBoolean

interface SearchTvLogoUseCase : RxUseCase2<SearchTvLogoParam, SearchTvLogoParam> {
    fun stop()
}

class SearchTvLogoUseCaseImpl(
    private val searchImage: SearchImage,
    private val dataManager: DataManager
) : SearchTvLogoUseCase {

    private var stop = false

    override val result: MutableLiveData<Result<SearchTvLogoParam>> = MutableLiveData()

    override fun run(param: SearchTvLogoParam): SearchTvLogoParam {
        val tvs = param.tvs
        val newTvs = tvs.filter {
            it.logo.isEmpty() && it.name.isNotEmpty()
        }
        newTvs.forEach {
            if (!stop) {
                val result = searchImage.search(it.name)
                if (result.isNotEmpty()) {
                    it.logo = result
                    dataManager.updateTv(it)
                    logd("${it.name} ${it.logo}")
                }
            }
        }
        return param.apply { this.tvs = newTvs }
    }

    override fun stop() {
        stop = true
    }
}

@Keep
data class SearchTvLogoParam(
    var tvs: List<Tv>
)