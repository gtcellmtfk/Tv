package com.bytebyte6.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.switchMap
import com.bytebyte6.common.BaseViewModel
import com.bytebyte6.common.onIo
import com.bytebyte6.common.onSingle
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.model.PlaylistWithTvs
import com.bytebyte6.usecase.DownloadTvUseCase
import com.bytebyte6.usecase.SearchTvLogoParam
import com.bytebyte6.usecase.SearchTvLogoUseCase
import com.bytebyte6.usecase.UpdateTvParam

class PlaylistViewModel(
    private val searchTvLogoUseCase: SearchTvLogoUseCase,
    private val downloadTvUseCase: DownloadTvUseCase,
    private val dataManager: DataManager
) : BaseViewModel(), Observer<PlaylistWithTvs> {

    private val playlistId = MutableLiveData<Long>()

    fun setPlaylistId(id: Long) {
        playlistId.value = id
        playlistWithTvs.observeForever(this)
    }

    val playlistWithTvs: LiveData<PlaylistWithTvs> = playlistId.switchMap {
        dataManager.playlistWithTvs(it)
    }

    val updateTv = downloadTvUseCase.result()

    override fun onChanged(playlistWithTvs1: PlaylistWithTvs) {
        playlistWithTvs.removeObserver(this)
        val tvs = playlistWithTvs1.tvs
        if (tvs.isNotEmpty()) {
            addDisposable(searchTvLogoUseCase.execute(SearchTvLogoParam(tvs)).onSingle())
        }
    }

    fun download(pos: Int, tv: Tv) {
        addDisposable(
            downloadTvUseCase.execute(UpdateTvParam(pos, tv.apply { download = true })).onIo()
        )
    }

    override fun onCleared() {
        super.onCleared()
        searchTvLogoUseCase.stop()
    }
}
