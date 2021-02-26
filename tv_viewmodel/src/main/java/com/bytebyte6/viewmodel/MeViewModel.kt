package com.bytebyte6.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.recyclerview.selection.Selection
import com.bytebyte6.common.BaseViewModel
import com.bytebyte6.common.Result
import com.bytebyte6.common.onIo
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.model.UserWithPlaylists
import com.bytebyte6.usecase.DeletePlaylistUseCase
import com.bytebyte6.usecase.ParseM3uUseCase
import com.bytebyte6.usecase.ParseParam

class MeViewModel(
    dataManager: DataManager,
    private val parseM3uUseCase: ParseM3uUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase
) : BaseViewModel() {

    private lateinit var userWithPlaylist: UserWithPlaylists

    val playlists = dataManager.user().switchMap { user ->
        dataManager.userWithPlaylist(user.userId).map { userWithPlaylists ->
            userWithPlaylist = userWithPlaylists
            userWithPlaylist.playlists
        }
    }

    val parseResult: LiveData<Result<Playlist>> = parseM3uUseCase.result()

    val deleteResult = deletePlaylistUseCase.result()

    fun parseM3u(it: Uri) {
        addDisposable(
            parseM3uUseCase.execute(ParseParam(it)).onIo()
        )
    }

    fun delete(selectedPos: Selection<Long>) {
        selectedPos.apply {
            val playlist = mutableListOf<Playlist>()
            selectedPos.forEach {
                playlist.add(userWithPlaylist.playlists[it.toInt()])
            }
            addDisposable(deletePlaylistUseCase.execute(playlist).onIo())
        }
    }
}
