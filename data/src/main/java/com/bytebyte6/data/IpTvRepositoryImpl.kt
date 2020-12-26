package com.bytebyte6.data

import androidx.lifecycle.LiveData
import com.bytebyte6.data.model.IpTv
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class IpTvRepositoryImpl(
    private val api: IpTvApi,
    private val tvDao: IpTvDao
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
        return tvDao.pagingLiveData(nextPage * 1000)
    }

    override fun init(loadData: LoadData<List<IpTv>>) {
        compositeDisposable.add(
            tvDao.getAnyIpTv()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it == null) {
                        refresh(loadData)
                    }
                }, {
                    refresh(loadData)
                    it.printStackTrace()
                })
        )
    }

    override fun clear() {
        compositeDisposable.dispose()
    }
}