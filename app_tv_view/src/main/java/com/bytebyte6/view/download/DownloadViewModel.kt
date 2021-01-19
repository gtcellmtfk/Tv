package com.bytebyte6.view.download

import android.content.Context
import androidx.lifecycle.Observer
import androidx.recyclerview.selection.Selection
import com.bytebyte6.base.mvi.Result
import com.bytebyte6.base.onIo
import com.bytebyte6.base_ui.BaseViewModel
import com.bytebyte6.data.model.TvAndDownload
import com.bytebyte6.usecase.DownloadListUseCase
import com.bytebyte6.usecase.UpdateTvParam
import com.bytebyte6.usecase.UpdateTvUseCase

class DownloadViewModel(
    private val context: Context,
    private val downloadListUseCase: DownloadListUseCase,
    private val updateTvUseCase: UpdateTvUseCase
) : BaseViewModel(), Observer<Result<List<TvAndDownload>>> {

    fun getRequest(pos: Int) = downloadListUseCase.getData()!![pos].download.request

    private fun getId(pos: Int) = downloadListUseCase.getData()!![pos].download.request.id

    fun delete(selection: Selection<Long>) {
        val data = downloadListUseCase.getData() ?: return
        selection.forEach {
            val pos = it.toInt()
            val tv = data[pos].tv
            tv.download = false
            addDisposable(updateTvUseCase.execute(UpdateTvParam(pos, tv)).onIo())
            RtmpDownloadService.removeDownload(context, getId(pos))
        }
    }

    val update = updateTvUseCase.result()

    val list = downloadListUseCase.result()

    fun loadList() {
        addDisposable(
            downloadListUseCase.execute("").onIo()
        )
    }

    init {
        downloadListUseCase.result().observeForever(this)
        loadList()
    }

    override fun onChanged(t: Result<List<TvAndDownload>>?) {
        downloadListUseCase.result().removeObserver(this)
        addDisposable(downloadListUseCase.interval("").onIo())
    }
}