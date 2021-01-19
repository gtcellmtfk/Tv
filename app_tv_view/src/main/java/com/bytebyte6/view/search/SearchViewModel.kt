package com.bytebyte6.view.search

import com.bytebyte6.base.mvi.isSuccess
import com.bytebyte6.base.onIo
import com.bytebyte6.base_ui.BaseViewModel
import com.bytebyte6.usecase.*

class SearchViewModel(
    private val searchTvUseCase: SearchTvUseCase,
    private val tvLogoSearchUseCase: TvLogoSearchUseCase,
    private val updateTvUseCase: UpdateTvUseCase

) : BaseViewModel() {

    val tvs = searchTvUseCase.result()

    val favorite = updateTvUseCase.result()

    val logoSearch = tvLogoSearchUseCase.result()

    fun search(key: CharSequence?) {
        if (!key.isNullOrEmpty()) {
            addDisposable(
                searchTvUseCase.execute(key.toString()).onIo()
            )
        }
    }

    fun searchLogo(pos: Int) {
        tvs.value?.apply {
            this.isSuccess()?.apply {
                val tvId = this[pos].tvId
                addDisposable(
                    tvLogoSearchUseCase.execute(SearchParam(id = tvId, pos = pos)).onIo()
                )
            }
        }
    }

    fun fav(pos: Int) {
        tvs.value?.apply {
            this.isSuccess()?.apply {
                val tv = this[pos]
                tv.favorite = !tv.favorite
                addDisposable(
                    updateTvUseCase.execute(UpdateTvParam(pos, tv)).onIo()
                )
            }
        }
    }
}

