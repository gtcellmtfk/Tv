package com.bytebyte6.viewmodel

import com.bytebyte6.common.BaseViewModel
import com.bytebyte6.data.dao.TvDao

class FavoriteViewModel(tvDao: TvDao) : BaseViewModel() {
    val fav = tvDao.allFavorite()
}