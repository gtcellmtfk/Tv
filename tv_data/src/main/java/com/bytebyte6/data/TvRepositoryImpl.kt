package com.bytebyte6.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.bytebyte6.base.LoadData
import com.bytebyte6.data.dao.PlaylistDao
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.data.dao.TvFtsDao
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.TvFts
import com.bytebyte6.data.model.Category
import com.bytebyte6.data.model.Country
import com.bytebyte6.data.model.Language
import com.bytebyte6.data.model.Languages
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class TvRepositoryImpl(
    private val context: Context,
    private val api: TvApi,
    private val tvDao: TvDao,
    private val tvFtsDao: TvFtsDao,
    private val playlistDao: PlaylistDao,
    private val converter: Converter
) : TvRepository {

    private val compositeDisposable = CompositeDisposable()

    override fun refresh(loadData: LoadData<List<Tv>>) {
        compositeDisposable.add(
            api.getList()
                .compose(loadData = loadData)
                .subscribe({
                    tvDao.insertAll(it)
                }, {
                    it.printStackTrace()
                })
        )
    }

    override fun init(loadData: LoadData<Int>) {
        compositeDisposable.add(
            tvDao.count()
                .compose(loadData = loadData)
                .subscribeOn(Schedulers.io())
                .subscribe({ count ->
                    if (count == 0) {
                        tvDao.insertAll(converter.getTvs(context).map {
                            if (it.category.isEmpty()) {
                                it.category = "Other"
                            }
                            if (it.language.isEmpty()) {
                                val list = mutableListOf(Language(languageName = "Other"))
                                it.language = list
                            }
                            it
                        })
                    }
                }, { it.printStackTrace() })
        )
    }

    override fun dispose() {
        compositeDisposable.dispose()
    }

    override fun liveDataByAllCategory(): LiveData<List<Category>> {
        val liveData = MediatorLiveData<List<Category>>()
        liveData.addSource(tvDao.liveDataByAllCategory()) { list ->
            compositeDisposable.add(
                Single.create<List<Category>> {
                    val result = mutableListOf<Category>()
                    list.forEach {
                        val count = tvDao.countByCategory(it)
                        result.add(Category(it, count))
                    }
                    liveData.postValue(result)
                }.subscribeOn(Schedulers.io())
                    .subscribe({}, {
                        it.printStackTrace()
                    })
            )
        }
        return liveData
    }

    override fun liveDataByAllLanguage(): LiveData<List<Languages>> {
        val liveData = MediatorLiveData<List<Languages>>()
        liveData.addSource(tvDao.liveDataByAllLanguage()) { list ->
            compositeDisposable.add(
                Single.create<List<Languages>> {
                    val languages = mutableListOf<Languages>()
                    list.forEach { json ->
                        val count = tvDao.countByLanguage(json)
                        val lang = Languages(count = count, languages = converter.toList(json))
                        languages.add(lang)
                    }
                    liveData.postValue(languages)
                }.subscribeOn(Schedulers.io())
                    .subscribe({}, {
                        it.printStackTrace()
                    })
            )
        }
        return liveData
    }

    override fun liveDataByAllCountry(): LiveData<List<Country>> {
        val liveData = MediatorLiveData<List<Country>>()
        liveData.addSource(tvDao.liveDataByAllCountry()) { list ->
            compositeDisposable.add(
                Single.create<List<Country>> {
                    val result = mutableListOf<Country>()
                    list.forEach {
                        val count = tvDao.countByCountry(it)
                        result.add(Country(countryName = it, count = count))
                    }
                    liveData.postValue(result)
                }.subscribeOn(Schedulers.io())
                    .subscribe({}, { it.printStackTrace() })
            )
        }
        return liveData
    }

    override fun search(key: String, loadData: LoadData<List<Tv>>) {
        compositeDisposable.add(
            tvFtsDao.search(key)
                .map<List<Tv>> {
                    TvFts.toIpTvs(it)
                }
                .compose(loadData = loadData)
                .subscribe({}, { it.printStackTrace() })
        )
    }

    override fun search(key: String): LiveData<List<Tv>> {
        return Transformations.map(tvFtsDao.searchLiveData(key)) {
            TvFts.toIpTvs(it)
        }
    }

    override fun tvs(playlistId: Long): LiveData<List<Tv>> {
        return playlistDao.tvsById(playlistId).map {
            it.tvs
        }
    }
}