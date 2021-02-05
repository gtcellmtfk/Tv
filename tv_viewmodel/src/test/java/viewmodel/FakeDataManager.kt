package viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.TvFts
import com.bytebyte6.data.model.PlaylistWithTvs
import com.bytebyte6.testdata.TestDataManager

object FakeDataManager : TestDataManager() {
    val playlist = Playlist(1, "P1")

    val tvs2 = mutableListOf(
        Tv(1, url = "A.Url", name = "A"),
        Tv(2, url = "B.Url", name = "B"),
        Tv(3, url = "C.Url", name = "C")
    )

    val playlistWithTvs = PlaylistWithTvs(playlist, tvs2)

    override fun playlistWithTvs(playlistId: Long): LiveData<PlaylistWithTvs> {
        return MutableLiveData(playlistWithTvs)
    }

    lateinit var up: Tv

    override fun updateTv(tv: Tv) {
        up = tv
    }

    override fun getFtsTvCount(key: String): Int {
        return 101
    }

    override fun ftsTvPaging(offset: Int, key: String, pageSize: Int): List<TvFts> {
        if (offset > ftss.size) {
            throw IllegalArgumentException("offset error!")
        }
        if (offset + pageSize > ftss.size) {
            return ftss.subList(offset, ftss.size)
        }
        return getFts().subList(offset, offset + pageSize)
    }

    override fun insertTv(tvs: List<Tv>): List<Long> {
        val temp = super.insertTv(tvs)
        ftss = getFts()
        return temp
    }

    lateinit var ftss: List<TvFts>

    private fun getFts(): MutableList<TvFts> {
        val list = mutableListOf<TvFts>()
        for (testTv in testTvs) {
            list.add(TvFts(1, testTv.url, name = testTv.name))
        }
        return list
    }
}