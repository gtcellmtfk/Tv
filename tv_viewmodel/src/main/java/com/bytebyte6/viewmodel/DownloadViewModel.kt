package com.bytebyte6.viewmodel

import androidx.lifecycle.Observer
import com.bytebyte6.base.*
import com.bytebyte6.usecase.DownloadListUseCase
import com.bytebyte6.usecase.TvAndDownload
import com.bytebyte6.usecase.UpdateTvParam
import com.bytebyte6.usecase.UpdateTvUseCase
import io.reactivex.rxjava3.disposables.Disposable

class DownloadViewModel(
    private val downloadListUseCase: DownloadListUseCase,
    private val updateTvUseCase: UpdateTvUseCase
) : BaseViewModel(), Observer<Result<List<TvAndDownload>>> {

    private var getDownloadList: Disposable? = null

    fun isStartInterval() = getDownloadList != null

    val downloadListResult = downloadListUseCase.result()

    val deleteResult = updateTvUseCase.result()

    init {
        downloadListUseCase.result().observeForever(this)
        loadDownloadList()
    }

    fun loadDownloadList() {
        addDisposable(
            downloadListUseCase.execute(Unit).onIo()
        )
    }

    private val resultObserver by lazy {
        object : ResultObserver<UpdateTvParam>() {
            override fun successOnce(data: UpdateTvParam, end: Boolean) {
                loadDownloadList()
                deleteResult.removeObserver(this)
            }
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
            updateTvUseCase.execute(UpdateTvParam(pos, tv)).onIo()
        )
    }

    fun startInterval() {
        downloadListResult.getSuccessData()?.apply {
            if (size > 0) {
                getDownloadList = downloadListUseCase.interval(Unit).onIo()
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
