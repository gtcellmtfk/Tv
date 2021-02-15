package com.bytebyte6.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.lib_test.getAwaitValue
import com.bytebyte6.lib_test.observeForTesting
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.User
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataManagerTest {

    private lateinit var dataManager: DataManager
    private lateinit var db: AppDatabase

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
        dataManager.insertUser(com.bytebyte6.testdata.defaultUser)
        assert(dataManager.hasUser())
        assert(dataManager.getCurrentUserIfNotExistCreate().name == com.bytebyte6.testdata.defaultUser.name)
        try {
            dataManager.insertUser(User(name = "B"))
        } catch (e: Exception) {
            assert(e == UnsupportedMultipleUserException)
        }
    }

    @Test
    fun deleteUser() {
        dataManager.insertUser(com.bytebyte6.testdata.defaultUser)
        assert(dataManager.hasUser())
        dataManager.deleteUser(dataManager.getCurrentUserIfNotExistCreate())
        assert(!dataManager.hasUser())
    }

    @Test
    fun updateUser() {
        dataManager.insertUser(com.bytebyte6.testdata.defaultUser)
        assert(dataManager.hasUser())
        dataManager.updateUser(dataManager.getCurrentUserIfNotExistCreate().apply { name = "B" })
        assert(dataManager.getCurrentUserIfNotExistCreate().name == "B")
    }

    @Test
    fun getUser() {
        assert(dataManager.getCurrentUserIfNotExistCreate().name == "Admin")
    }

    @Test
    fun hasUser() {
        assert(!dataManager.hasUser())
        dataManager.getCurrentUserIfNotExistCreate()
        assert(dataManager.hasUser())
    }

    @Test
    fun user() {
        dataManager.insertUser(com.bytebyte6.testdata.defaultUser)
        val user = dataManager.user()
        user.observeForTesting {
            assert(user.getAwaitValue()!!.name == com.bytebyte6.testdata.defaultUser.name)
        }
    }

    //Country
    @Test
    fun insertCountry() {
        dataManager.insertCountry(com.bytebyte6.testdata.china)
        assert(dataManager.getCountryCount() != 0)
        assert(dataManager.getCountries().size == 1)
        assert(dataManager.getCountries()[0].name == com.bytebyte6.testdata.china.name)
    }

    @Test
    fun insertCountrys() {
        dataManager.insertCountry(mutableListOf(
            com.bytebyte6.testdata.china,
            com.bytebyte6.testdata.usa,
            com.bytebyte6.testdata.kor
        ))
        assert(dataManager.getCountryCount() == 3)
    }

    @Test
    fun updateCountry() {
        dataManager.insertCountry(com.bytebyte6.testdata.china)
        assert(dataManager.getCountryCount() == 1)
        val newChina = dataManager.getCountries()[0].apply { name = "CHINESE" }
        dataManager.updateCountry(newChina)
        assert(dataManager.getCountries()[0].name == newChina.name)
    }

    @Test
    fun updateCountries() {
        var tvs: List<Country> = mutableListOf(
            com.bytebyte6.testdata.china,
            com.bytebyte6.testdata.usa,
            com.bytebyte6.testdata.kor
        )
        dataManager.insertCountry(tvs)
        tvs = dataManager.getCountries()
        dataManager.updateCountry(tvs.apply {
            this[0].name = "AA"
            this[1].name = "BB"
            this[2].name = "CC"
        })
        val value = dataManager.getCountries()
        assert(value[0].name == "AA")
        assert(value[1].name == "BB")
        assert(value[2].name == "CC")
    }

    @Test
    fun countries() {
        val liveData = dataManager.countries()
        dataManager.insertCountry(com.bytebyte6.testdata.china)
        liveData.observeForTesting {
            assert(liveData.getAwaitValue()!![0].name == com.bytebyte6.testdata.china.name)
        }
    }

    @Test
    fun getCountries() {
        dataManager.insertCountry(com.bytebyte6.testdata.china)
        assert(dataManager.getCountries().isNotEmpty())
    }

    @Test
    fun getCountryIdByName() {
        dataManager.insertCountry(com.bytebyte6.testdata.china)
        val countryId = dataManager.getCountryIdByName(com.bytebyte6.testdata.china.name)
        assert(countryId > -1)
    }

    @Test
    fun getCountryCount() {
        assert(dataManager.getCountryCount() == 0)
        dataManager.insertCountry(com.bytebyte6.testdata.china)
        assert(dataManager.getCountryCount() == 1)
    }

    //Tv
    @Test
    fun insertTv() {
        dataManager.insertTv(com.bytebyte6.testdata.tv1)
        assert(dataManager.getTvCount() == 1)
        assert(dataManager.getTvs()[0].url == com.bytebyte6.testdata.tv1.url)
    }

    @Test
    fun insertTvs() {
        dataManager.insertTv(mutableListOf(
            com.bytebyte6.testdata.tv1,
            com.bytebyte6.testdata.tv2,
            com.bytebyte6.testdata.tv3
        ))
        assert(dataManager.getTvCount() == 3)
    }

    @Test
    fun deleteTv() {
        dataManager.insertTv(mutableListOf(
            com.bytebyte6.testdata.tv1,
            com.bytebyte6.testdata.tv2,
            com.bytebyte6.testdata.tv3
        ))
        dataManager.deleteTv(dataManager.getTvByUrl(com.bytebyte6.testdata.tv2.url)!!)
        assert(dataManager.getTvCount() == 2)
    }

    @Test
    fun updateTv() {
        val id = dataManager.insertTv(com.bytebyte6.testdata.tv1)
        dataManager.updateTv(com.bytebyte6.testdata.tv1.apply {
            tvId = id
            url = "WTF.url"
        })
        assert(dataManager.getTvByUrl("WTF.url") != null)
    }

    @Test
    fun updateTvs() {
        var tvs: List<Tv> = mutableListOf(
            com.bytebyte6.testdata.tv1,
            com.bytebyte6.testdata.tv2,
            com.bytebyte6.testdata.tv3
        )
        dataManager.insertTv(tvs)
        tvs = dataManager.getTvs()
        dataManager.updateTv(tvs.apply {
            this[0].url = "AA"
            this[1].url = "BB"
            this[2].url = "CC"
        })
        assert(dataManager.getTvByUrl("AA") != null)
        assert(dataManager.getTvByUrl("BB") != null)
        assert(dataManager.getTvByUrl("CC") != null)
    }


    @Test
    fun getTvByUrl() {
        assert(dataManager.getTvByUrl("") == null)
        dataManager.insertTv(com.bytebyte6.testdata.tv1)
        assert(dataManager.getTvByUrl(com.bytebyte6.testdata.tv1.url) != null)
    }

    @Test
    fun getTvById() {
        val id = dataManager.insertTv(com.bytebyte6.testdata.tv1)
        val data = dataManager.getTvById(id)
        assert(data.name == com.bytebyte6.testdata.tv1.name)
        assert(data.url == com.bytebyte6.testdata.tv1.url)
    }

    @Test
    fun getTvs() {
        dataManager.insertTv(mutableListOf(
            com.bytebyte6.testdata.tv1,
            com.bytebyte6.testdata.tv2,
            com.bytebyte6.testdata.tv3
        ))
        val size = dataManager.getTvs().size
        assert(size == 3)
    }

    @Test
    fun getTvCount() {
        dataManager.insertTv(mutableListOf(
            com.bytebyte6.testdata.tv1,
            com.bytebyte6.testdata.tv2,
            com.bytebyte6.testdata.tv3
        ))
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
        dataManager.insertTv(mutableListOf(
            com.bytebyte6.testdata.tv1,
            com.bytebyte6.testdata.tv2,
            com.bytebyte6.testdata.tv3
        ))
        val tvsByKeyword = dataManager.getTvsByKeyword(com.bytebyte6.testdata.tv1.url)
        assert(tvsByKeyword.size == 1)
        assert(tvsByKeyword[0].url == com.bytebyte6.testdata.tv1.url)
        val tvsByKeyword1 = dataManager.getTvsByKeyword(com.bytebyte6.testdata.tv2.name)
        assert(tvsByKeyword1.size == 1)
        assert(tvsByKeyword1[0].name == com.bytebyte6.testdata.tv2.name)
    }

    @Test
    fun getFtsTvCount() {
        dataManager.insertTv(mutableListOf(
            com.bytebyte6.testdata.tv1,
            com.bytebyte6.testdata.tv2,
            com.bytebyte6.testdata.tv3
        ))
        assert(dataManager.getFtsTvCount(com.bytebyte6.testdata.tv1.countryName) == 1)
    }

    @Test
    fun ftsTvCount() {
        dataManager.insertTv(mutableListOf(
            com.bytebyte6.testdata.tv1,
            com.bytebyte6.testdata.tv2,
            com.bytebyte6.testdata.tv3
        ))
        val liveData = dataManager.ftsTvCount(com.bytebyte6.testdata.tv1.countryName)
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
        val id = dataManager.insertPlaylist(com.bytebyte6.testdata.playlist1)
        assert(dataManager.getPlaylist(id).playlistName == com.bytebyte6.testdata.playlist1.playlistName)
    }

    @Test
    fun deletePlaylist() {
        val id = dataManager.insertPlaylist(com.bytebyte6.testdata.playlist1)
        val playlist = dataManager.getPlaylist(id)
        assert(playlist.playlistName == com.bytebyte6.testdata.playlist1.playlistName)
        dataManager.deletePlaylist(playlist)
        assert(dataManager.getPlaylistCount() == 0)
    }

    @Test
    fun deletePlaylists() {
        dataManager.insertPlaylist(com.bytebyte6.testdata.playlist1)
        dataManager.insertPlaylist(com.bytebyte6.testdata.playlist2)
        dataManager.insertPlaylist(com.bytebyte6.testdata.playlist3)
        assert(dataManager.getPlaylistCount() == 3)
        dataManager.deletePlaylist(dataManager.getPlaylists())
        assert(dataManager.getPlaylistCount() == 0)
    }

    @Test
    fun updatePlaylist() {
        dataManager.insertPlaylist(com.bytebyte6.testdata.playlist1)
        assert(dataManager.getPlaylistCount() == 1)
        val playlist = dataManager.getPlaylists()[0].apply { playlistName = "FFF" }
        dataManager.updatePlaylist(playlist)
        assert(dataManager.getPlaylist(playlist.playlistId).playlistName == "FFF")
    }

    @Test
    fun getPlaylist() {
        val id = dataManager.insertPlaylist(com.bytebyte6.testdata.playlist1)
        assert(dataManager.getPlaylistCount() == 1)
        assert(dataManager.getPlaylist(id).playlistName == com.bytebyte6.testdata.playlist1.playlistName)
    }

    @Test
    fun allFavoriteTv() {
        dataManager.insertTv(com.bytebyte6.testdata.tv1.apply { favorite = true })
        val v = dataManager.allFavoriteTv()
        v.observeForTesting {
            assert(v.getAwaitValue()!!.size == 1)
        }
    }

    @Test
    fun allLanguage() {
        dataManager.insertLanguages(
            mutableListOf(
                com.bytebyte6.testdata.lang1,
                com.bytebyte6.testdata.lang2
            )
        )
        val allLanguage = dataManager.allLanguage()
        allLanguage.observeForTesting {
            val awaitValue = allLanguage.getAwaitValue()
            assert(awaitValue!!.size == 2)
        }
    }

    @Test
    fun allCategory() {
        dataManager.insertCategory(
            mutableListOf(
                com.bytebyte6.testdata.c1,
                com.bytebyte6.testdata.c2,
                com.bytebyte6.testdata.c3
            )
        )
        val allCategory = dataManager.allCategory()
        allCategory.observeForTesting {
            assert(allCategory.getAwaitValue()!!.size == 3)
        }
    }
}