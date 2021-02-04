package com.bytebyte6.viewmodel

import com.bytebyte6.common.BaseViewModel
import com.bytebyte6.data.DataManager

class FavoriteViewModel(dataManager: DataManager) : BaseViewModel() {
    val allFav = dataManager.allFavoriteTv()
}