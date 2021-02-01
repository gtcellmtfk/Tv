package com.bytebyte6.viewmodel

import com.bytebyte6.base.BaseViewModel
import com.bytebyte6.base.getSuccessData
import com.bytebyte6.base.onIo
import com.bytebyte6.usecase.*

class SearchViewModel(
    private val searchTvUseCase: SearchTvUseCase,
    private val tvLogoSearchUseCase: TvLogoSearchUseCase,
    private val updateTvUseCase: UpdateTvUseCase
) : BaseViewModel() {

    val searchResult = searchTvUseCase.result()

    val favoriteResult = updateTvUseCase.result()

    val logoUrlSearchResult = tvLogoSearchUseCase.result()

    fun search(key: CharSequence?) {
        if (!key.isNullOrEmpty()) {
            addDisposable(
                searchTvUseCase.execute(key.toString()).onIo()
            )
        }
    }

    fun searchLogo(pos: Int) {
        searchResult.getSuccessData()?.apply {
            val tvId = this[pos].tvId
            addDisposable(
                tvLogoSearchUseCase.execute(SearchParam(id = tvId, pos = pos)).onIo()
            )
        }
    }

    fun fav(pos: Int) {
        searchResult.getSuccessData()?.apply {
            val tv = this[pos]
            tv.favorite = !tv.favorite
            addDisposable(
                updateTvUseCase.execute(UpdateTvParam(pos, tv)).onIo()
            )
        }
    }
}

