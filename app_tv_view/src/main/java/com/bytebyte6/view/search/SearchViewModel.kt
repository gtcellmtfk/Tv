package com.bytebyte6.view.search

import com.bytebyte6.base.mvi.isSuccess
import com.bytebyte6.base_ui.BaseViewModel
import com.bytebyte6.view.usecase.SearchTvUseCase
import com.bytebyte6.view.usecase.TvLogoSearchUseCase

class SearchViewModel(
    private val searchTvUseCase: SearchTvUseCase,
    private val tvLogoSearchUseCase: TvLogoSearchUseCase
) : BaseViewModel() {

    val tvs = searchTvUseCase.result()

    fun search(key: CharSequence?) {
        if (!key.isNullOrEmpty()) {
            addDisposable(
                searchTvUseCase.execute(key.toString())
            )
        }
    }

    fun searchLogo(pos:Int){
        tvs.value?.apply {
            this.isSuccess()?.apply {
                val tvId= this[pos].tvId
                addDisposable(
                    tvLogoSearchUseCase.execute(tvId)
                )
            }
        }
    }
}

