package com.bytebyte6.app_tv_viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.recyclerview.selection.Selection
import com.bytebyte6.base.Result
import com.bytebyte6.base.BaseViewModel
import com.bytebyte6.base.onIo
import com.bytebyte6.data.dao.UserDao
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.model.UserWithPlaylists
import com.bytebyte6.usecase.*

class MeViewModel(
    private val userDao: UserDao,
    private val parseM3uUseCase: ParseM3uUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase
) : BaseViewModel(){

    private lateinit var userWithPlaylist: UserWithPlaylists

    val playlistNames = userDao.user().switchMap { user ->
        userDao.userWithPlaylists(user.userId).map { userWithPlaylists ->
            userWithPlaylist = userWithPlaylists
            userWithPlaylist.playlists
        }
    }

    val parseResult: LiveData<Result<Playlist>> = parseM3uUseCase.result()

    val deleteResult = deletePlaylistUseCase.result()

    fun getPlaylistId(pos: Int): Long {
        return userWithPlaylist.playlists[pos].playlistId
    }

    fun parseM3u(it: Uri) {
        addDisposable(
            parseM3uUseCase.execute(it).onIo()
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
