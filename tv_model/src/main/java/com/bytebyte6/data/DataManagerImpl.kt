package com.bytebyte6.data

import androidx.lifecycle.LiveData
import com.bytebyte6.data.entity.*
import com.bytebyte6.data.model.*

class DataManagerImpl(
    appDatabase: AppDatabase
) : DataManager {

    private val tvDao = appDatabase.tvDao()
    private val tvFtsDao = appDatabase.tvFtsDao()
    private val userDao = appDatabase.userDao()
    private val playlistDao = appDatabase.playlistDao()
    private val userPlaylistCrossRefDao = appDatabase.userPlaylistCrossRefDao()
    private val playlistTvCrossRefDao = appDatabase.playlistTvCrossRefDao()
    private val countryDao = appDatabase.countryDao()
    private val playlistWithTvsCache = PlaylistWithTvsCache(this)

    override fun insertUser(user: User): Long {
        if (hasUser()) {
            throw UnsupportedMultipleUserException
        }
        return userDao.insert(user)
    }

    override fun insertUser(users: List<User>): List<Long> {
        return userDao.insert(users)
    }

    override fun deleteUser(user: User) {
        userDao.delete(user)
    }

    override fun updateUser(user: User) {
        userDao.update(user)
    }

    override fun getCurrentUserIfNotExistCreate(): User {
        if (!hasUser()) {
            insertUser(User(name = "Admin"))
        }
        return userDao.getUser()
    }

    override fun hasUser(): Boolean {
        return userDao.getCount() > 0
    }

    override fun user(): LiveData<User> {
        return userDao.user()
    }

    override fun getUsers(): List<User> {
        return userDao.getUsers()
    }

    override fun insertCountry(country: Country): Long {
        return countryDao.insert(country)
    }

    override fun insertCountry(countries: List<Country>): List<Long> {
        return countryDao.insert(countries)
    }

    override fun updateCountry(country: Country) {
        countryDao.update(country)
    }

    override fun updateCountry(countries: List<Country>) {
        countryDao.update(countries)
    }

    override fun countries(): LiveData<List<Country>> {
        return countryDao.countries()
    }

    override fun getCountries(): List<Country> {
        return countryDao.getCountries()
    }

    override fun getImageEmptyCountries(): List<Country> {
        return countryDao.getCountriesByImage("")
    }

    override fun getCountryIdByName(name: String): Long {
        return countryDao.getIdByName(name)
    }

    override fun getCountryCount(): Int {
        return countryDao.getCount()
    }

    override fun insertTv(tvs: List<Tv>): List<Long> {
        return tvDao.insert(tvs)
    }

    override fun insertTv(tv: Tv): Long {
        return tvDao.insert(tv)
    }

    override fun deleteTv(tv: Tv) {
        tvDao.delete(tv)
    }

    override fun deleteTv(tvs: List<Tv>) {
        tvDao.delete(tvs)
    }

    override fun updateTv(tv: Tv) {
        tvDao.update(tv)
    }

    override fun updateTv(tvs: List<Tv>) {
        tvDao.update(tvs)
    }

    override fun getTvByUrl(url: String): Tv? {
        return tvDao.getTvByUrl(url)
    }

    override fun getTvById(id: Long): Tv {
        return tvDao.getTv(id)
    }

    override fun getTvs(): List<Tv> {
        return tvDao.getTvs()
    }

    override fun getLogoEmptyTvs(): List<Tv> {
        return tvDao.getTvsByLogo("")
    }

    override fun getTvCount(): Int {
        return tvDao.getCount()
    }

    override fun tvPaging(offset: Int, pageSize: Int): List<Tv> {
        return tvDao.paging(offset, pageSize)
    }

    override fun getTvsByKeyword(key: String): List<Tv> {
        return tvFtsDao.search(key)
    }

    override fun tvsByKeyword(key: String): LiveData<List<Tv>> {
        return tvFtsDao.tvs(key)
    }

    override fun getFtsTvCount(key: String): Int {
        return tvFtsDao.getCount(key)
    }

    override fun ftsTvCount(key: String): LiveData<Int> {
        return tvFtsDao.count(key)
    }

    override fun ftsTvPaging(offset: Int, key: String, pageSize: Int): List<Tv> {
        return tvFtsDao.paging(offset, key, pageSize)
    }

    override fun insertPlaylist(playlist: Playlist): Long {
        return playlistDao.insert(playlist)
    }

    override fun insertPlaylist(playlists: List<Playlist>): List<Long> {
        return playlistDao.insert(playlists)
    }

    override fun deletePlaylist(playlist: Playlist) {
        return playlistDao.delete(playlist)
    }

    override fun deletePlaylist(playlists: List<Playlist>) {
        playlistDao.delete(playlists)
    }

    override fun updatePlaylist(playlist: Playlist) {
        playlistDao.update(playlist)
    }

    override fun getPlaylists(): List<Playlist> {
        return playlistDao.getPlaylists()
    }

    override fun getPlaylist(id: Long): Playlist {
        return playlistDao.getPlaylist(id)
    }

    override fun getPlaylistCount(): Int {
        return playlistDao.getCount()
    }

    override fun userWithPlaylist(userId: Long): LiveData<UserWithPlaylists> {
        return userDao.userWithPlaylists(userId)
    }

    override fun getUserWithPlaylist(): UserWithPlaylists {
        return userDao.getPlaylistsByUserId(userDao.getUser().userId)
    }

    override fun getTvWithPlaylistss(): List<TvWithPlaylists> {
        return tvDao.getTvWithPlaylistss()
    }

    override fun playlistWithTvs(playlistId: Long): LiveData<PlaylistWithTvs> {
        return playlistDao.playlistWithTvs(playlistId)
    }

    override fun getPlaylistWithTvs(playlistId: Long): PlaylistWithTvs {
        return playlistDao.getPlaylistWithTvsById(playlistId)
    }

    override fun getPlaylistsWithTvss(): List<PlaylistWithTvs> {
        return playlistDao.getPlaylistsWithTvss()
    }

    override fun getTvsByPlaylistId(playlistId: Long, page: Int): List<Tv> {
        return playlistWithTvsCache.getTvsByPlaylistId(playlistId, page)
    }

    override fun updatePlaylistCache(playlistId: Long, newList: List<Tv>, page: Int) {
        playlistWithTvsCache.updateTvsByPlaylistId(playlistId, newList, page)
    }

    override fun getTvCountByPlaylistId(playlistId: Long): Int {
        return playlistWithTvsCache.getTvCountByPlaylistId(playlistId)
    }

    override fun getUserWithPlaylists(): List<UserWithPlaylists> {
        return userDao.getUsersWithPlaylists()
    }

    override fun getPlaylistsWithUsers(): List<PlaylistWithUsers> {
        return playlistDao.getPlaylistsWithUsers()
    }

    override fun crossRefUserWithPlaylist(userPlaylistCrossRef: UserPlaylistCrossRef): Long {
        return userPlaylistCrossRefDao.insert(userPlaylistCrossRef)
    }

    override fun crossRefUserWithPlaylists(userPlaylistCrossRefs: List<UserPlaylistCrossRef>): List<Long> {
        return userPlaylistCrossRefDao.insert(userPlaylistCrossRefs)
    }

    override fun crossRefPlaylistWithTv(playlistTvCrossRefs: List<PlaylistTvCrossRef>): List<Long> {
        return playlistTvCrossRefDao.insert(playlistTvCrossRefs)
    }

    override fun allFavoriteTv(): LiveData<List<Tv>> {
        return tvDao.allFavorite()
    }

    override fun allLanguage(): LiveData<List<Languages>> {
        return tvDao.allLanguage()
    }

    override fun allCategory(): LiveData<List<Category>> {
        return tvDao.allCategory()
    }
}