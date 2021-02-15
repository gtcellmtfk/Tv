package com.bytebyte6.data

import androidx.lifecycle.LiveData
import com.bytebyte6.data.entity.*
import com.bytebyte6.data.model.*


interface DataManager {
    //User
    fun insertUser(user: User): Long
    fun insertUser(users: List<User>): List<Long>
    fun deleteUser(user: User)
    fun updateUser(user: User)
    fun getCurrentUserIfNotExistCreate(): User
    fun hasUser(): Boolean
    fun user(): LiveData<User>
    fun getUsers(): List<User>

    //Language
    fun insertLanguages(languages: List<Language>)
    fun getLangCount(): Int

    //Country
    fun insertCountry(countries: List<Country>): List<Long>
    fun insertCountry(country: Country): Long
    fun updateCountry(country: Country)
    fun updateCountry(countries: List<Country>)
    fun countries(): LiveData<List<Country>>
    fun getCountries(): List<Country>
    fun getImageEmptyCountries(): List<Country>
    fun getCountryIdByName(name: String): Long
    fun getCountryCount(): Int
    fun getCountryByCode(code: String): Country

    //Tv
    fun insertTv(tvs: List<Tv>): List<Long>
    fun insertTv(tv: Tv): Long
    fun deleteTv(tv: Tv)
    fun deleteTv(tvs: List<Tv>)
    fun updateTv(tv: Tv)
    fun updateTv(tvs: List<Tv>)
    fun getTvByUrl(url: String): Tv?
    fun getTvById(id: Long): Tv
    fun getTvs(): List<Tv>
    fun getLogoEmptyTvs(): List<Tv>
    fun getTvCount(): Int
    fun tvPaging(offset: Int, pageSize: Int): List<Tv>

    //TvFts
    fun getTvsByKeyword(key: String): List<Tv>
    fun tvsByKeyword(key: String): LiveData<List<Tv>>
    fun getFtsTvCount(key: String): Int
    fun ftsTvCount(key: String): LiveData<Int>
    fun ftsTvPaging(offset: Int, key: String, pageSize: Int): List<Tv>

    //Playlist
    fun insertPlaylist(playlist: Playlist): Long
    fun insertPlaylist(playlists: List<Playlist>): List<Long>
    fun deletePlaylist(playlist: Playlist)
    fun deletePlaylist(playlists: List<Playlist>)
    fun updatePlaylist(playlist: Playlist)
    fun getPlaylist(id: Long): Playlist
    fun getPlaylists(): List<Playlist>
    fun getPlaylistCount(): Int

    //User With Playlist
    fun userWithPlaylist(userId: Long): LiveData<UserWithPlaylists>
    fun getUserWithPlaylist(): UserWithPlaylists
    fun getUserWithPlaylists(): List<UserWithPlaylists>
    fun getPlaylistsWithUsers(): List<PlaylistWithUsers>

    //Tv With Playlist
    fun getTvWithPlaylistss(): List<TvWithPlaylists>

    //Playlist With Tvs
    fun playlistWithTvs(playlistId: Long): LiveData<PlaylistWithTvs>
    fun getPlaylistWithTvs(playlistId: Long): PlaylistWithTvs
    fun getPlaylistsWithTvss(): List<PlaylistWithTvs>
    fun getTvsByPlaylistId(playlistId: Long, page: Int): List<Tv>
    fun updatePlaylistCache(playlistId: Long, newList: List<Tv>, page: Int)
    fun getTvCountByPlaylistId(playlistId: Long): Int

    //CrossRef
    fun crossRefUserWithPlaylist(userPlaylistCrossRef: UserPlaylistCrossRef): Long
    fun crossRefUserWithPlaylists(userPlaylistCrossRefs: List<UserPlaylistCrossRef>): List<Long>
    fun crossRefPlaylistWithTv(playlistTvCrossRefs: List<PlaylistTvCrossRef>): List<Long>

    //Category
    fun insertCategory(categories: List<Category>)
    fun getCategoryCount(): Int

    fun allFavoriteTv(): LiveData<List<Tv>>
    fun allLanguage(): LiveData<List<Language>>
    fun allCategory(): LiveData<List<Category>>
}