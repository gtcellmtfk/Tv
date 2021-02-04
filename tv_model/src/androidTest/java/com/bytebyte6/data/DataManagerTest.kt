package com.bytebyte6.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.common.getAwaitValue
import com.bytebyte6.common.observeForTesting
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.User
import com.bytebyte6.data.model.Language
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataManagerTest {

    private lateinit var dataManager: DataManager
    private lateinit var db: AppDatabase
    private val china = Country(name = "CHINA")
    private val usa = Country(name = "US")
    private val kor = Country(name = "KOR")
    private val lang1 = Language("CHINESE", "CN")
    private val lang2 = Language("ENGLISH", "EN")
    private val tv1 =
        Tv(
            name = "A",
            url = "A.url",
            countryName = china.name,
            language = mutableListOf(lang1),
            category = "A"
        )
    private val tv2 =
        Tv(
            name = "B",
            url = "B.url",
            countryName = usa.name,
            language = mutableListOf(lang2),
            category = "B"
        )
    private val tv3 = Tv(
        category = "C",
        name = "C",
        url = "C.url",
        countryName = kor.name,
        language = mutableListOf(lang1, lang2)
    )
    private val user = User(name = "admin")
    private val playlist1 = Playlist(playlistName = "P1")
    private val playlist2 = Playlist(playlistName = "P2")
    private val playlist3 = Playlist(playlistName = "P3")

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dataManager = DataManagerImpl(db)
    }

    @After
    fun close() {
        db.close()
    }

    //User
    @Test
    fun insertUser() {
        dataManager.insertUser(user)
        assert(dataManager.hasUser())
        assert(dataManager.getUser().name == user.name)
        try {
            dataManager.insertUser(User(name = "B"))
        } catch (e: Exception) {
            assert(e == UnsupportedMultipleUserException)
        }
    }

    @Test
    fun deleteUser() {
        dataManager.insertUser(user)
        assert(dataManager.hasUser())
        dataManager.deleteUser(dataManager.getUser())
        assert(!dataManager.hasUser())
    }

    @Test
    fun updateUser() {
        dataManager.insertUser(user)
        assert(dataManager.hasUser())
        dataManager.updateUser(dataManager.getUser().apply { name = "B" })
        assert(dataManager.getUser().name == "B")
    }

    @Test
    fun getUser() {
        assert(dataManager.getUser().name == "Admin")
    }

    @Test
    fun hasUser() {
        assert(!dataManager.hasUser())
        dataManager.getUser()
        assert(dataManager.hasUser())
    }

    @Test
    fun user() {
        dataManager.insertUser(this.user)
        val user = dataManager.user()
        user.observeForTesting {
            assert(user.getAwaitValue()!!.name == this.user.name)
        }
    }

    //Country
    @Test
    fun insertCountry() {
        dataManager.insertCountry(china)
        assert(dataManager.getCountryCount() != 0)
        assert(dataManager.getCountries().size == 1)
        assert(dataManager.getCountries()[0].name == china.name)
    }

    @Test
    fun insertCountrys() {
        dataManager.insertCountry(mutableListOf(china, usa, kor))
        assert(dataManager.getCountryCount() == 3)
    }

    @Test
    fun updateCountry() {
        dataManager.insertCountry(china)
        assert(dataManager.getCountryCount() == 1)
        val newChina = dataManager.getCountries()[0].apply { name = "CHINESE" }
        dataManager.updateCountry(newChina)
        assert(dataManager.getCountries()[0].name == newChina.name)
    }

    @Test
    fun countries() {
        val liveData = dataManager.countries()
        dataManager.insertCountry(china)
        liveData.observeForTesting {
            assert(liveData.getAwaitValue()!![0].name == china.name)
        }
    }

    @Test
    fun getCountries() {
        dataManager.insertCountry(china)
        assert(dataManager.getCountries().isNotEmpty())
    }

    @Test
    fun getCountryIdByName() {
        dataManager.insertCountry(china)
        val countryId = dataManager.getCountryIdByName(china.name)
        assert(countryId > -1)
    }

    @Test
    fun getCountryCount() {
        assert(dataManager.getCountryCount() == 0)
        dataManager.insertCountry(china)
        assert(dataManager.getCountryCount() == 1)
    }

    //Tv
    @Test
    fun insertTv() {
        dataManager.insertTv(tv1)
        assert(dataManager.getTvCount() == 1)
        assert(dataManager.getTvs()[0].url == tv1.url)
    }

    @Test
    fun insertTvs() {
        dataManager.insertTv(mutableListOf(tv1, tv2, tv3))
        assert(dataManager.getTvCount() == 3)
    }

    @Test
    fun deleteTv() {
        dataManager.insertTv(mutableListOf(tv1, tv2, tv3))
        dataManager.deleteTv(dataManager.getTvByUrl(tv2.url)!!)
        assert(dataManager.getTvCount() == 2)
    }

    @Test
    fun updateTv() {
        val id = dataManager.insertTv(tv1)
        dataManager.updateTv(tv1.apply {
            tvId = id
            url = "WTF.url"
        })
        assert(dataManager.getTvByUrl("WTF.url") != null)
    }

    @Test
    fun getTvByUrl() {
        assert(dataManager.getTvByUrl("") == null)
        dataManager.insertTv(tv1)
        assert(dataManager.getTvByUrl(tv1.url) != null)
    }

    @Test
    fun getTvById() {
        val id = dataManager.insertTv(tv1)
        val data = dataManager.getTvById(id)
        assert(data.name == tv1.name)
        assert(data.url == tv1.url)
    }

    @Test
    fun getTvs() {
        dataManager.insertTv(mutableListOf(tv1, tv2, tv3))
        val size = dataManager.getTvs().size
        assert(size == 3)
    }

    @Test
    fun getTvCount() {
        dataManager.insertTv(mutableListOf(tv1, tv2, tv3))
        assert(dataManager.getTvCount() == 3)
    }

    @Test
    fun tvPaging() {
        val tvs21 = mutableListOf<Tv>()
        for (i in 0..20) {
            tvs21.add(Tv(name = "$i", url = "$i.url"))
        }
        dataManager.insertTv(tvs21)
        assert(dataManager.getTvCount() == 21)
        val one = dataManager.tvPaging(0, 20)
        assert(one.size == 20)
        val two = dataManager.tvPaging(20, 20)
        assert(two.size == 1)
        val three = dataManager.tvPaging(40, 20)
        assert(three.isEmpty())
    }

    //TvFts
    @Test
    fun getTvsByKeyword() {
        dataManager.insertTv(mutableListOf(tv1, tv2, tv3))
        val tvsByKeyword = dataManager.getTvsByKeyword(tv1.url)
        assert(tvsByKeyword.size == 1)
        assert(tvsByKeyword[0].url == tv1.url)
        val tvsByKeyword1 = dataManager.getTvsByKeyword(tv2.name)
        assert(tvsByKeyword1.size == 1)
        assert(tvsByKeyword1[0].name == tv2.name)
    }

    @Test
    fun getFtsTvCount() {
        dataManager.insertTv(mutableListOf(tv1, tv2, tv3))
        assert(dataManager.getFtsTvCount(tv1.countryName) == 1)
    }

    @Test
    fun ftsTvCount() {
        dataManager.insertTv(mutableListOf(tv1, tv2, tv3))
        val liveData = dataManager.ftsTvCount(tv1.countryName)
        liveData.observeForTesting {
            assert(liveData.getAwaitValue() == 1)
        }
    }

    @Test
    fun ftsTvPaging() {
        val tvs21 = mutableListOf<Tv>()
        for (i in 0..20) {
            tvs21.add(Tv(name = "CCTV $i", url = "$i.url"))
        }
        dataManager.insertTv(tvs21)
        assert(dataManager.getTvCount() == 21)
        val one = dataManager.ftsTvPaging(0, "CCTV", 20)
        assert(one.size == 20)
        val two = dataManager.ftsTvPaging(20, "CCTV", 20)
        assert(two.size == 1)
        val three = dataManager.ftsTvPaging(40, "CCTV", 20)
        assert(three.isEmpty())
    }

    //Playlist
    @Test
    fun insertPlaylist() {
        val id = dataManager.insertPlaylist(playlist1)
        assert(dataManager.getPlaylist(id).playlistName == playlist1.playlistName)
    }

    @Test
    fun deletePlaylist() {
        val id = dataManager.insertPlaylist(playlist1)
        val playlist = dataManager.getPlaylist(id)
        assert(playlist.playlistName == playlist1.playlistName)
        dataManager.deletePlaylist(playlist)
        assert(dataManager.getPlaylistCount() == 0)
    }

    @Test
    fun deletePlaylists() {
        val id1 = dataManager.insertPlaylist(playlist1)
        val id2 = dataManager.insertPlaylist(playlist2)
        val id3 = dataManager.insertPlaylist(playlist3)
        assert(dataManager.getPlaylistCount() == 3)
        playlist1.playlistId = id1
        playlist2.playlistId = id2
        playlist3.playlistId = id3
        dataManager.deletePlaylist(mutableListOf(playlist1, playlist2, playlist3))
        assert(dataManager.getPlaylistCount() == 0)
    }

    @Test
    fun updatePlaylist() {
        dataManager.insertPlaylist(playlist1)
        assert(dataManager.getPlaylistCount() == 1)
        val playlist = dataManager.getPlaylists()[0].apply { playlistName = "FFF" }
        dataManager.updatePlaylist(playlist)
        assert(dataManager.getPlaylist(playlist.playlistId).playlistName == "FFF")
    }

    @Test
    fun getPlaylist() {
        val id = dataManager.insertPlaylist(playlist1)
        assert(dataManager.getPlaylistCount() == 1)
        assert(dataManager.getPlaylist(id).playlistName == playlist1.playlistName)
    }

    @Test
    fun allFavoriteTv() {
        dataManager.insertTv(tv1.apply { favorite = true })
        val v = dataManager.allFavoriteTv()
        v.observeForTesting {
            assert(v.getAwaitValue()!!.size == 1)
        }
    }

    @Test
    fun allLanguage() {
        dataManager.insertTv(mutableListOf(tv1, tv2, tv3))
        val allLanguage = dataManager.allLanguage()
        allLanguage.observeForTesting {
            assert(allLanguage.getAwaitValue()!!.size == 3)
        }
    }

    @Test
    fun allCategory() {
        dataManager.insertTv(mutableListOf(tv1, tv2, tv3))
        val allCategory = dataManager.allCategory()
        allCategory.observeForTesting {
            assert(allCategory.getAwaitValue()!!.size == 3)
        }
    }
}