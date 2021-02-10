package com.bytebyte6.viewmodel

import androidx.lifecycle.Observer
import com.bytebyte6.common.*
import com.bytebyte6.usecase.DownloadListUseCase
import com.bytebyte6.usecase.DownloadTvUseCase
import com.bytebyte6.usecase.TvAndDownload
import com.bytebyte6.usecase.UpdateTvParam
import io.reactivex.rxjava3.disposables.Disposable

class DownloadViewModel(
    private val downloadListUseCase: DownloadListUseCase,
    private val downloadTvUseCase: DownloadTvUseCase
) : BaseViewModel(), Observer<Result<List<TvAndDownload>>> {

    private var getDownloadList: Disposable? = null

    fun isStartInterval() = getDownloadList != null

    val downloadListResult = downloadListUseCase.result()

    val deleteResult = downloadTvUseCase.result()

    init {
        downloadListUseCase.result().observeForever(this)
        loadDownloadList()
    }

    fun loadDownloadList() {
        addDisposable(
            downloadListUseCase.execute(Unit).onIo()
        )
    }

    private val resultObserver = object : Observer<Result<UpdateTvParam>> {
        override fun onChanged(t: Result<UpdateTvParam>?) {
            loadDownloadList()
            deleteResult.removeObserver(this)
        }
    }

    fun deleteDownload(pos: Int) {
        val data = downloadListResult.getSuccessData() ?: return
        if (data.isEmpty()) {
            return
        }
        val tv = data[pos].tv
        tv.download = false
        deleteResult.observeForever(resultObserver)
        addDisposable(
            downloadTvUseCase.execute(UpdateTvParam(pos, tv)).onIo()
        )
    }

    fun startInterval() {
        downloadListResult.getSuccessData()?.apply {
            if (size > 0) {
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
