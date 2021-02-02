package viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.data.dao.PlaylistDao
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.model.PlaylistWithTvs
import com.bytebyte6.data.model.PlaylistWithUsers

object FakePlaylistDao : PlaylistDao {

    val playlist = Playlist(1, "P1")
    val tvs = mutableListOf(
        Tv(1, url = "A.Url", name = "A"),
        Tv(2, url = "B.Url", name = "B"),
        Tv(3, url = "C.Url", name = "C")
    )
    val playlistWithTvs = PlaylistWithTvs(playlist, tvs)

    override fun getPlaylistsWithTvss(): List<PlaylistWithTvs> {
        return listOf(playlistWithTvs)
    }

    override fun getPlaylistWithTvsById(playlistId: Long): PlaylistWithTvs {
        return playlistWithTvs
    }

    override fun getPlaylistsWithUsers(): List<PlaylistWithUsers> {
        return emptyList()
    }

    override fun getPlaylists(): List<Playlist> {
        return listOf(playlist)
    }

    val playlistWithTvsL = MutableLiveData(playlistWithTvs)
    
    override fun playlistWithTvs(playlistId: Long): LiveData<PlaylistWithTvs> {
        return playlistWithTvsL
    }

    override fun insert(data: Playlist): Long {
        return 1
    }

    override fun insert(list: List<Playlist>): List<Long> {
        return emptyList()
    }

    override fun delete(data: Playlist) {
        
    }

    override fun delete(list: List<Playlist>) {
        
    }

    override fun update(data: Playlist) {
        
    }

    override fun update(list: List<Playlist>) {
        
    }
}