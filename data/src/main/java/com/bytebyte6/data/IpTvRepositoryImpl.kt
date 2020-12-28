package com.bytebyte6.data

import androidx.lifecycle.LiveData
import com.bytebyte6.base.LoadData
import com.bytebyte6.data.model.IpTv
import com.bytebyte6.data.model.WrapLanguages
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class IpTvRepositoryImpl(
    private val api: IpTvApi,
    private val tvDao: IpTvDao,
    private val converter: Converter
) : IpTvRepository {

    private val compositeDisposable = CompositeDisposable()

    private var nextPage = -1

    override fun refresh(loadData: LoadData<List<IpTv>>) {
        compositeDisposable.add(
            api.getList()
                .compose(loadData)
                .subscribe({
                    tvDao.insertAll(it)
                }, {
                    it.printStackTrace()
                })
        )
    }

    override fun liveData(): LiveData<List<IpTv>> {
        return tvDao.allLiveData()
    }

    override fun nextPage(): LiveData<List<IpTv>> {
        nextPage += 1
        return tvDao.pagingLiveData(nextPage * PAGE_SIZE)
    }

    override fun init(loadData: LoadData<List<IpTv>>) {
        compositeDisposable.add(
            tvDao.getAnyIpTv()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    loadData.success(emptyList())
                }, {
                    refresh(loadData)
                })
        )
    }

    override fun clear() {
        compositeDisposable.dispose()
    }

    override fun liveDataByAllCategory(): LiveData<List<String>> {
        return tvDao.liveDataByAllCategory()
    }

    override fun liveDataByAllLanguage(): LiveData<List<WrapLanguages>> {
        return tvDao.liveDataByAllLanguage()
    }

    override fun liveDataByAllCountry(): LiveData<List<String>> {
        return tvDao.liveDataByAllCountry()
    }

    override fun liveDataByCategory(category: String): LiveData<List<IpTv>> {
        return tvDao.liveDataByCategory(category)
    }

    override fun liveDataByLanguage(language: String): LiveData<List<IpTv>> {
        return tvDao.liveDataByLanguage(language)
    }

    override fun liveDataByCountry(countryName: String): LiveData<List<IpTv>> {
        return tvDao.liveDataByCountry(countryName)
    }
}