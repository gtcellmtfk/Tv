package viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.model.Category
import com.bytebyte6.data.model.Languages
import com.bytebyte6.data.model.TvWithPlaylists

object FakeTvDao : TvDao {

    val defaultTv = Tv(name = "A", url = "A.url")

    override fun getTvWithPlaylistss(): List<TvWithPlaylists> {
        return emptyList()
    }

    override fun paging(offset: Int, pageSize: Int): List<Tv> {
        return emptyList()
    }

    override fun getCount(): Int {
        return 1
    }

    override fun getTvs(): List<Tv> {
        return emptyList()
    }

    override fun getDownloadTvs(): List<Tv> {
        return emptyList()
    }

    override fun getTv(id: Long): Tv {
        return defaultTv
    }

    override fun getTvByUrl(url: String): Tv? {
        return null
    }

    override fun allCategory(): LiveData<List<Category>> {

        return MutableLiveData(emptyList())
    }

    override fun allLanguage(): LiveData<List<Languages>> {
        return MutableLiveData(emptyList())

    }

    override fun allFavorite(): LiveData<List<Tv>> {
        return MutableLiveData(emptyList())

    }

    override fun insert(data: Tv): Long {
        return 1
    }

    override fun insert(list: List<Tv>): List<Long> {
        return emptyList()
    }

    override fun delete(data: Tv) {

    }

    override fun delete(list: List<Tv>) {

    }

    var update: Tv = Tv()
    override fun update(data: Tv) {
        update = data
    }

    override fun update(list: List<Tv>) {

    }
}