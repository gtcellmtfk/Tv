package com.bytebyte6.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.room.rxjava3.EmptyResultSetException
import com.bytebyte6.base.LoadData
import com.bytebyte6.data.model.*
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class IpTvRepositoryImpl(
    private val api: IpTvApi,
    private val tvDao: IpTvDao,
    private val tvFtsDao: IpTvFtsDao,
    private val context: Context,
    private val converter: Converter
) : IpTvRepository {

    private val compositeDisposable = CompositeDisposable()

    override fun refresh(loadData: LoadData<List<IpTv>>) {
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

    override fun init(loadData: LoadData<IpTv>) {
        compositeDisposable.add(
            tvDao.getAnyIpTv()
                .compose(loadData = loadData)
                .subscribeOn(Schedulers.io())
                .doOnError {
                    if (it is EmptyResultSetException) {
                        tvDao.insertAll(getIpTVs())
                    }
                }
                .subscribe({}, { it.printStackTrace() })
        )
    }

    private fun getIpTVs(): List<IpTv> {
        val json: String = context.assets.open("channels.json")
            .bufferedReader()
            .use { it.readText() }
        return converter.gson.fromJson(
            json,
            object : TypeToken<List<IpTv>>() {}.type
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
                    .subscribe()
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
                    .subscribe()
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
                    .subscribe()
            )
        }
        return liveData
    }

    override fun liveDataByCategory(category: String): LiveData<List<IpTv>> {
        return tvDao.liveDataByCategory(category)
    }

    override fun liveDataByLanguage(languages: Languages): LiveData<List<IpTv>> {
        val json = converter.toJson(languages.languages)
        return tvDao.liveDataByLanguage(json)
    }

    override fun liveDataByCountry(countryName: String): LiveData<List<IpTv>> {
        return tvDao.liveDataByCountry(countryName)
    }

    override fun search(key: String, loadData: LoadData<List<IpTv>>) {
        compositeDisposable.add(
            tvFtsDao.search(key)
                .map<List<IpTv>> {
                    IpTvFts.toIpTvs(it)
                }
                .compose(loadData = loadData)
                .subscribe()
        )
    }
}