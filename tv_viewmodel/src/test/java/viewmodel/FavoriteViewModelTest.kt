package viewmodel


import com.bytebyte6.viewmodel.FavoriteViewModel
import org.junit.Test

class FavoriteViewModelTest {
    @Test
    fun test(){
        val favoriteViewModel=FavoriteViewModel(FakeTvDao)
        assert(favoriteViewModel.fav.value!!.isEmpty())
    }
}