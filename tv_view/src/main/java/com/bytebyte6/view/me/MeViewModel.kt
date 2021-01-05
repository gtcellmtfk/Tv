package com.bytebyte6.view.me

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.bytebyte6.base.BaseViewModelDelegate
import com.bytebyte6.base.mvi.Result
import com.bytebyte6.base.mvi.success
import com.bytebyte6.data.TvRepository
import com.bytebyte6.view.usecase.ParseM3uUseCase
import com.bytebyte6.view.usecase.PlaylistByUserIdUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MeViewModel(
    private val baseViewModelDelegate: BaseViewModelDelegate,
    private val playlistByUserIdUseCase: PlaylistByUserIdUseCase,
    private val parseM3uUseCase: ParseM3uUseCase,
    private val tvRepository: TvRepository
) : ViewModel(),
    BaseViewModelDelegate by baseViewModelDelegate {

    private val compositeDisposable = CompositeDisposable()

    private val userWithPlaylist = playlistByUserIdUseCase.liveData()

    val playlistNames =
        userWithPlaylist.map { response ->
            val playlistNames = mutableListOf<String>()
            val playlists = response.success()?.playlists
            playlists?.forEach {
                playlistNames.add(it.playlistName)
            }
            playlistNames
        }

    val playlistId: LiveData<Result<Long>> = parseM3uUseCase.liveData()

    init {
        compositeDisposable.add(
            playlistByUserIdUseCase.execute(0)
        )
    }

    fun tvs(playlistId: Long) = tvRepository.tvs(playlistId)

    fun getPlaylistId(pos: Int): Long {
        return userWithPlaylist.value!!.success()!!.playlists[pos].playlistId
    }

    fun parseM3u(it: Uri) {
        compositeDisposable.add(
            parseM3uUseCase.execute(it)
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun getPlaylistName(): String {
        return parseM3uUseCase.playlistName
    }


}
