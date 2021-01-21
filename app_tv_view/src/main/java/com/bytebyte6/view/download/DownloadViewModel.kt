package com.bytebyte6.view.download

import androidx.lifecycle.Observer
import com.bytebyte6.base.mvi.Result
import com.bytebyte6.base.mvi.runIfNotHandled
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

    val list = downloadListUseCase.result()

    init {
        downloadListUseCase.result().observeForever(this)
        loadList()
    }

    private fun loadList() {
        addDisposable(
            downloadListUseCase.execute("").onIo()
        )
    }

    fun delete(pos: Int) {
        val data = downloadListUseCase.getData() ?: return
        val tv = data[pos].tv
        tv.download = false
        val observer = object : Observer<Result<UpdateTvParam>> {
            override fun onChanged(it: Result<UpdateTvParam>) {
                it.runIfNotHandled {
                    loadList()
                    updateTvUseCase.result().removeObserver(this)
                }
            }
        }
        updateTvUseCase.result().observeForever(observer)
        addDisposable(
            updateTvUseCase.execute(UpdateTvParam(pos, tv)).onIo()
        )
    }

    fun start() {
        pause()
        getDownloadList = downloadListUseCase.interval("").onIo()
        addDisposable(getDownloadList!!)
    }

    fun pause() {
        getDownloadList?.dispose()
        loadList()
    }

    override fun onChanged(t: Result<List<TvAndDownload>>?) {
        downloadListUseCase.result().removeObserver(this)
        start()
    }
}