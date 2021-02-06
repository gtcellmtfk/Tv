package com.bytebyte6.viewmodel

import com.bytebyte6.common.BaseViewModel
import com.bytebyte6.common.onIo
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.usecase.FavoriteTvUseCase
import com.bytebyte6.usecase.UpdateTvParam

class FavoriteViewModel(
    dataManager: DataManager,
    private val favoriteTvUseCase: FavoriteTvUseCase
) : BaseViewModel() {

    val allFav = dataManager.allFavoriteTv()

    val cancelResult = favoriteTvUseCase.result()

    fun favorite(pos: Int, tv: Tv) {
        addDisposable(
            favoriteTvUseCase.execute(UpdateTvParam(pos, tv)).onIo()
        )
    }

    fun restoreFavorite(tv: Tv) {
        addDisposable(
            favoriteTvUseCase.execute(UpdateTvParam(-1, tv)).onIo()
        )
    }
}