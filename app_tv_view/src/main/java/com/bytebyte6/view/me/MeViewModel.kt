package com.bytebyte6.view.me

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.bytebyte6.base.Event
import com.bytebyte6.base.mvi.Result
import com.bytebyte6.data.dao.PlaylistDao
import com.bytebyte6.data.dao.UserDao
import com.bytebyte6.data.model.UserWithPlaylists
import com.bytebyte6.view.usecase.ParseM3uUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MeViewModel(
    private val userDao: UserDao,
    private val parseM3uUseCase: ParseM3uUseCase,
    private val playlistDao: PlaylistDao
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private lateinit var userWithPlaylist: UserWithPlaylists

    val playlistNames = userDao.liveData().switchMap { user ->
        userDao.playlistsLiveData(user.userId).map { userWithPlaylists ->
            userWithPlaylist = userWithPlaylists

            userWithPlaylists.playlists
        }
    }

    val playlistId: LiveData<Event<Result<Long>>> = parseM3uUseCase.eventLiveData()

    fun tvs(playlistId: Long) = playlistDao.tvsById(playlistId).map {
        it.tvs
    }

    fun getPlaylistId(pos: Int): Long {
        return userWithPlaylist.playlists[pos].playlistId
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
