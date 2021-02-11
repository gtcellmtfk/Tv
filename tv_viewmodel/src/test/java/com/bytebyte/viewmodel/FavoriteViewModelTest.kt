package com.bytebyte.viewmodel

import com.bytebyte6.usecase.FavoriteTvUseCase
import com.bytebyte6.viewmodel.FavoriteViewModel
import org.junit.Test

class FavoriteViewModelTest {
    @Test
    fun test(){
        val favoriteViewModel=FavoriteViewModel(FakeDataManager, FavoriteTvUseCase(FakeDataManager))
        assert(favoriteViewModel.allFav.value==null)
    }
}