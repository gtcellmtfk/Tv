package viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.*
import com.bytebyte6.data.model.Category
import com.bytebyte6.data.model.Languages
import com.bytebyte6.data.model.PlaylistWithTvs
import com.bytebyte6.data.model.UserWithPlaylists

object FakeDataManager : DataManager {

    override fun insertUser(user: User): Long {
        return FakeUserDao.insert(user)
    }

    override fun deleteUser(user: User) {
        FakeUserDao.delete(user)
    }

    override fun updateUser(user: User) {
        FakeUserDao.update(user)
    }

    override fun getUser(): User {
        return FakeUserDao.getUser()
    }

    override fun hasUser(): Boolean {
        return FakeUserDao.getCount() != 0
    }

    override fun user(): LiveData<User> {
        return FakeUserDao.user().map {
            if (it == null) {
                val user = FakeUserDao.defaultUser
                user
            } else {
                it
            }
        }
    }

    override fun insertCountry(countries: List<Country>): List<Long> {
        return emptyList()
    }

    override fun insertCountry(country: Country): Long {
        return 1
    }

    override fun updateCountry(country: Country) {

    }

    override fun countries(): LiveData<List<Country>> {
        return MutableLiveData(emptyList())
    }

    override fun getCountries(): List<Country> {
        return emptyList()
    }

    override fun getCountryIdByName(name: String): Long {
        return 1
    }

    override fun insertTv(tvs: List<Tv>): List<Long> {
        return FakeTvDao.insert(tvs)
    }

    override fun insertTv(tv: Tv): Long {
        return FakeTvDao.insert(tv)
    }

    override fun deleteTv(tv: Tv) {
        FakeTvDao.delete(tv)
    }

    override fun updateTv(tv: Tv) {
        FakeTvDao.update(tv)
    }

    override fun getTvByUrl(url: String): Tv? {
        return FakeTvDao.getTvByUrl(url)
    }

    override fun getTvById(id: Long): Tv {
        return FakeTvDao.getTv(id)
    }

    override fun getTvs(): List<Tv> {
        return FakeTvDao.getTvs()
    }

    override fun getTvCount(): Int {
        return FakeTvDao.getCount()
    }

    override fun tvPaging(offset: Int, pageSize: Int): List<Tv> {
        return FakeTvDao.paging(offset, pageSize)
    }

    override fun getTvsByKeyword(key: String): List<Tv> {
        return emptyList()
    }

    override fun getFtsTvCount(key: String): Int {
        return 1
    }

    override fun ftsTvCount(key: String): LiveData<Int> {
        return MutableLiveData()
    }

    override fun ftsTvPaging(offset: Int, key: String, pageSize: Int): List<TvFts> {
        return emptyList()
    }

    override fun insertPlaylist(playlist: Playlist): Long {
        return FakePlaylistDao.insert(playlist)
    }

    override fun deletePlaylist(playlist: Playlist) {
        FakePlaylistDao.delete(playlist)
    }

    override fun deletePlaylist(playlists: List<Playlist>) {
        FakePlaylistDao.delete(playlists)
    }

    override fun updatePlaylist(playlist: Playlist) {
        FakePlaylistDao.update(playlist)
    }

    override fun getPlaylist(id: Long): Playlist {
        return FakePlaylistDao.getPlaylist(id)
    }

    override fun userWithPlaylist(userId: Long): LiveData<UserWithPlaylists> {
        return FakeUserDao.userWithPlaylists(userId)
    }

    override fun getUserWithPlaylist(): UserWithPlaylists {
        return FakeUserDao.getPlaylistsByUserId(FakeUserDao.defaultUser.userId)
    }

    override fun playlistWithTvs(playlistId: Long): LiveData<PlaylistWithTvs> {
        return FakePlaylistDao.playlistWithTvs(playlistId)
    }

    override fun getPlaylistWithTvs(playlistId: Long): PlaylistWithTvs {
        return FakePlaylistDao.getPlaylistWithTvsById(playlistId)
    }

    override fun crossRefUserWithPlaylist(userPlaylistCrossRef: UserPlaylistCrossRef): Long {
        return 1
    }

    override fun crossRefPlaylistWithTv(playlistTvCrossRefs: List<PlaylistTvCrossRef>): List<Long> {
        return emptyList()
    }

    override fun allFavoriteTv(): LiveData<List<Tv>> {
        return MutableLiveData(emptyList())
    }

    override fun allLanguage(): LiveData<List<Languages>> {
        return MutableLiveData()
    }

    override fun allCategory(): LiveData<List<Category>> {
        return MutableLiveData()
    }
}