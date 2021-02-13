package com.bytebyte6.testdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.*
import com.bytebyte6.data.model.*
import java.util.*

val c1 get() = Category("A")
val c2 get() = Category("B")
val c3 get() = Category("C")

val china get() = Country(countryId = 1, name = "CHINA", code = "CN")
val usa get() = Country(countryId = 2, name = "US", code = "Us")
val kor get() = Country(countryId = 3, name = "KOR", code = "Ko")
val countries
    get() = mutableListOf(
        china,
        usa,
        kor
    )
val lang1 get() = Language("CHINESE", "CN")
val lang2 get() = Language("ENGLISH", "EN")
val tv1
    get() =
        Tv(
            country = china,
            name = "A",
            url = "A.url",
            countryName = china.name,
            language = Language.NAME,
            category = "A"
        )
val tv2
    get() =
        Tv(
            country = usa,
            name = "B",
            url = "B.url",
            countryName = usa.name,
            language = Language.NAME,
            category = "B"
        )
val tv3
    get() = Tv(
        country = kor,
        category = "C",
        name = "C",
        url = "C.url",
        countryName = kor.name,
        language = Language.NAME
    )
val tvs
    get() = mutableListOf(
        tv1,
        tv2,
        tv3
    )
val defaultUser get() = User(name = "admin")
val playlist1 get() = Playlist(playlistName = "P1")
val playlist2 get() = Playlist(playlistName = "P2")
val playlist3 get() = Playlist(playlistName = "P3")

abstract class TestDataManager : DataManager {

    val testCountries = mutableListOf<Country>()

    val testTvs = mutableListOf<Tv>()

    val testUsers = mutableListOf<User>()

    override fun insertLanguages(languages: List<Language>) {

    }

    override fun getLangCount(): Int {
        return 1
    }

    override fun getCountryByCode(code: String): Country {
        return Country.DEFAULT
    }

    override fun insertCategory(categories: List<Category>) {

    }

    override fun getCategoryCount(): Int {
        return 1
    }

    override fun insertUser(user: User): Long {
        testUsers.add(user)
        return testUsers.size.toLong()
    }

    override fun insertUser(users: List<User>): List<Long> {
        return emptyList()
    }

    override fun deleteUser(user: User) {

    }

    override fun updateUser(user: User) {
        testUsers[0] = user
    }

    override fun getCurrentUserIfNotExistCreate(): User {
        if (testUsers.size == 0) {
            val element = User(name = UUID.randomUUID().toString())
            testUsers.add(element)
            return element
        }
        return testUsers[0]
    }

    override fun hasUser(): Boolean {
        return testUsers.isNotEmpty()
    }

    override fun user(): LiveData<User> {
        if (hasUser()) {
            return MutableLiveData(testUsers[0])
        }
        return MutableLiveData()
    }

    override fun getUsers(): List<User> {
        return testUsers
    }

    override fun insertCountry(countries: List<Country>): List<Long> {
        testCountries.addAll(countries)
        return emptyList()
    }

    override fun insertCountry(country: Country): Long {
        return 1
    }

    override fun updateCountry(country: Country) {
        testCountries.add(country)
    }

    override fun updateCountry(countries: List<Country>) {
        testCountries.addAll(countries)
    }

    override fun countries(): LiveData<List<Country>> {
        return MutableLiveData()
    }

    override fun getCountries(): List<Country> {
        return emptyList()
    }

    override fun getImageEmptyCountries(): List<Country> {
        return emptyList()
    }

    override fun getCountryIdByName(name: String): Long {
        for (testCountry in testCountries) {
            if (testCountry.name == name) {
                return testCountry.countryId
            }
        }
        return -1
    }

    override fun getCountryCount(): Int {
        return testCountries.size
    }

    override fun insertTv(tvs: List<Tv>): List<Long> {
        testTvs.addAll(tvs)
        return emptyList()
    }

    override fun insertTv(tv: Tv): Long {
        return 1
    }

    override fun deleteTv(tv: Tv) {

    }

    override fun deleteTv(tvs: List<Tv>) {

    }

    override fun updateTv(tv: Tv) {
        testTvs.add(tv)
    }

    override fun updateTv(tvs: List<Tv>) {

    }

    override fun getTvByUrl(url: String): Tv? {
        return null
    }

    override fun getTvById(id: Long): Tv {
        if (testTvs.isNotEmpty()) {
            return testTvs[id.toInt()]
        }
        return Tv()
    }

    override fun getTvs(): List<Tv> {
        return emptyList()
    }

    override fun getLogoEmptyTvs(): List<Tv> {
        return emptyList()
    }

    override fun getTvCount(): Int {
        return testTvs.size
    }

    override fun tvPaging(offset: Int, pageSize: Int): List<Tv> {
        return emptyList()
    }

    override fun getTvsByKeyword(key: String): List<Tv> {
        return emptyList()
    }

    override fun tvsByKeyword(key: String): LiveData<List<Tv>> {
        return MutableLiveData(testTvs)
    }

    override fun getFtsTvCount(key: String): Int {
        return 1
    }

    override fun ftsTvCount(key: String): LiveData<Int> {
        return MutableLiveData()
    }

    override fun ftsTvPaging(offset: Int, key: String, pageSize: Int): List<Tv> {
        return emptyList()
    }

    override fun insertPlaylist(playlist: Playlist): Long {
        return 1
    }

    override fun insertPlaylist(playlists: List<Playlist>): List<Long> {
        return emptyList()
    }

    override fun deletePlaylist(playlist: Playlist) {

    }

    override fun deletePlaylist(playlists: List<Playlist>) {

    }

    override fun updatePlaylist(playlist: Playlist) {

    }

    override fun getPlaylist(id: Long): Playlist {
        return Playlist(playlistName = "")
    }

    override fun getPlaylists(): List<Playlist> {
        return emptyList()
    }

    override fun getPlaylistCount(): Int {
        return 1
    }

    override fun userWithPlaylist(userId: Long): LiveData<UserWithPlaylists> {
        return MutableLiveData()
    }

    override fun getUserWithPlaylist(): UserWithPlaylists {
        return UserWithPlaylists(User())
    }

    override fun getUserWithPlaylists(): List<UserWithPlaylists> {
        return emptyList()
    }

    override fun getPlaylistsWithUsers(): List<PlaylistWithUsers> {
        return emptyList()
    }

    override fun getTvWithPlaylistss(): List<TvWithPlaylists> {
        return emptyList()
    }

    override fun playlistWithTvs(playlistId: Long): LiveData<PlaylistWithTvs> {
        return MutableLiveData()
    }

    override fun getPlaylistWithTvs(playlistId: Long): PlaylistWithTvs {
        return PlaylistWithTvs(Playlist(0, ""))
    }

    override fun getPlaylistsWithTvss(): List<PlaylistWithTvs> {
        return emptyList()
    }

    override fun getTvsByPlaylistId(playlistId: Long, page: Int): List<Tv> {
        return emptyList()
    }

    override fun updatePlaylistCache(playlistId: Long, newList: List<Tv>, page: Int) {

    }

    override fun getTvCountByPlaylistId(playlistId: Long): Int {
        return 1
    }

    override fun crossRefUserWithPlaylist(userPlaylistCrossRef: UserPlaylistCrossRef): Long {
        return 1
    }

    override fun crossRefUserWithPlaylists(userPlaylistCrossRefs: List<UserPlaylistCrossRef>): List<Long> {
        return emptyList()
    }

    override fun crossRefPlaylistWithTv(playlistTvCrossRefs: List<PlaylistTvCrossRef>): List<Long> {
        return emptyList()
    }

    override fun allFavoriteTv(): LiveData<List<Tv>> {
        return MutableLiveData()
    }

    override fun allLanguage(): LiveData<List<Language>> {
        return MutableLiveData()
    }

    override fun allCategory(): LiveData<List<Category>> {
        return MutableLiveData()
    }

}