package com.bytebyte6.view.download

import androidx.lifecycle.Observer
import com.bytebyte6.base.mvi.Result
import com.bytebyte6.base.mvi.ResultObserver
import com.bytebyte6.base.onIo
import com.bytebyte6.base_ui.BaseViewModel
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

    val downloadList = downloadListUseCase.result()

    init {
        downloadListUseCase.result().observeForever(this)
        loadDownloadList()
    }

    private fun loadDownloadList() {
        addDisposable(
            downloadListUseCase.execute("").onIo()
        )
    }

    fun deleteDownload(pos: Int) {
        val data = downloadListUseCase.getData() ?: return
        val tv = data[pos].tv
        tv.download = false
        val resultObserver = object : ResultObserver<UpdateTvParam>() {
            override fun successOnce(data: UpdateTvParam, end: Boolean) {
                loadDownloadList()
                updateTvUseCase.result().removeObserver(this)
            }
        }
        updateTvUseCase.result().observeForever(resultObserver)
        addDisposable(
            updateTvUseCase.execute(UpdateTvParam(pos, tv)).onIo()
        )
    }

    fun startInterval() {
        pauseInterval()
        getDownloadList = downloadListUseCase.interval("").onIo()
        addDisposable(getDownloadList!!)
    }

    fun pauseInterval() {
        getDownloadList?.dispose()
        loadDownloadList()
    }

    override fun onChanged(t: Result<List<TvAndDownload>>?) {
        downloadListUseCase.result().removeObserver(this)
        startInterval()
    }
}