package com.bytebyte6.viewmodel

import androidx.lifecycle.Observer
import com.bytebyte6.common.*
import com.bytebyte6.usecase.DownloadListUseCase
import com.bytebyte6.usecase.TvAndDownload
import com.bytebyte6.usecase.UpdateTvParam
import io.reactivex.rxjava3.disposables.Disposable

class DownloadViewModel(
    private val networkHelper: NetworkHelper,
    private val downloadListUseCase: DownloadListUseCase
) : BaseViewModel(), Observer<Result<List<TvAndDownload>>> {

    private var getDownloadList: Disposable? = null

    fun isStartInterval() = getDownloadList != null

    val downloadListResult = downloadListUseCase.result()

    private val netObs = Observer<NetworkHelper.NetworkType> {
        when (it) {
            NetworkHelper.NetworkType.WIFI -> startInterval()
            else -> Unit
        }
    }

    init {
        downloadListUseCase.result().observeForever(this)
        loadDownloadList()
        networkHelper.networkType.observeForever(netObs)
    }

    override fun onCleared() {
        super.onCleared()
        networkHelper.networkType.removeObserver(netObs)
    }

    fun loadDownloadList() {
        addDisposable(downloadListUseCase.execute(Unit).onIo())
    }

    fun startInterval() {
        downloadListResult.getSuccessData()?.apply {
            if (size > 0) {
                getDownloadList?.dispose()
                getDownloadList = downloadListUseCase.interval(Unit, 2).onIo()
                addDisposable(getDownloadList!!)
            }
        }
    }

    fun pauseInterval() {
        getDownloadList?.dispose()
        getDownloadList = null
        loadDownloadList()
    }

    override fun onChanged(result: Result<List<TvAndDownload>>) {
        val success = result.isSuccess()
        if (success != null) {
            startInterval()
            downloadListUseCase.result().removeObserver(this)
        }
    }
}
