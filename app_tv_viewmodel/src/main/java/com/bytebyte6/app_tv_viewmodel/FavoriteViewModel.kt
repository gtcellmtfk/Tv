package com.bytebyte6.app_tv_viewmodel

import com.bytebyte6.base.BaseViewModel
import com.bytebyte6.data.dao.TvDao

class FavoriteViewModel(tvDao: TvDao) : BaseViewModel() {
    val fav = tvDao.allFavorite()
}