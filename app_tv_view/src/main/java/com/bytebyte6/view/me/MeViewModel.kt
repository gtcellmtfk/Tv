package com.bytebyte6.view.me

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.recyclerview.selection.Selection
import com.bytebyte6.base.mvi.Result
import com.bytebyte6.base_ui.BaseViewModel
import com.bytebyte6.data.dao.PlaylistDao
import com.bytebyte6.data.dao.UserDao
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.model.UserWithPlaylists
import com.bytebyte6.view.usecase.DeletePlaylistUseCase
import com.bytebyte6.view.usecase.ParseM3uUseCase
import com.bytebyte6.view.usecase.TvLogoSearchUseCase

class MeViewModel(
    private val userDao: UserDao,
    private val parseM3uUseCase: ParseM3uUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
    private val tvLogoSearchUseCase: TvLogoSearchUseCase,
    private val playlistDao: PlaylistDao
) : BaseViewModel() {

    private lateinit var userWithPlaylist: UserWithPlaylists

    val playlistNames = userDao.user().switchMap { user ->
        userDao.userWithPlaylists(user.userId).map { userWithPlaylists ->
            userWithPlaylist = userWithPlaylists

            userWithPlaylists.playlists
        }
    }

    val playlistId: LiveData<Result<Long>> = parseM3uUseCase.result()

    val deleteAction = deletePlaylistUseCase.result()

    fun searchLogo(pos: Int) {
        addDisposable(
            tvLogoSearchUseCase.execute(tvs[pos].tvId)
        )
    }

    private lateinit var tvs: List<Tv>

    fun tvs(playlistId: Long) = playlistDao.playlistWithTvs(playlistId).map {
        tvs = it.tvs
        tvs
    }

    fun getPlaylistId(pos: Int): Long {
        return userWithPlaylist.playlists[pos].playlistId
    }

    fun parseM3u(it: Uri) {
        addDisposable(
            parseM3uUseCase.execute(it)
        )
    }

    fun getPlaylistName(): String {
        return parseM3uUseCase.playlistName
    }

    fun delete(selectedPos: Selection<Long>?) {
        selectedPos?.apply {
            val playlist = mutableListOf<Playlist>()
            selectedPos.forEach {
                playlist.add(userWithPlaylist.playlists[it.toInt()])
            }
            addDisposable(deletePlaylistUseCase.execute(playlist))
        }
    }
}
