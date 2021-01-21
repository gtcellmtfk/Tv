package com.bytebyte6.view.me

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.recyclerview.selection.Selection
import com.bytebyte6.base.mvi.Result
import com.bytebyte6.base.onIo
import com.bytebyte6.base_ui.BaseViewModel
import com.bytebyte6.data.dao.PlaylistDao
import com.bytebyte6.data.dao.UserDao
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.model.UserWithPlaylists
import com.bytebyte6.usecase.*

class PlaylistViewModel(
    private val tvLogoSearchUseCase: TvLogoSearchUseCase,
    private val updateTvUseCase: UpdateTvUseCase,
    private val playlistDao: PlaylistDao
) : BaseViewModel(){

    val updateTv = updateTvUseCase.result()

    fun searchLogo(pos: Int) {
        addDisposable(
            tvLogoSearchUseCase.execute(SearchParam(id = tvs[pos].tvId, pos = pos)).onIo()
        )
    }

    private lateinit var tvs: List<Tv>

    fun getTv(pos: Int) = tvs[pos]

    fun tvs(playlistId: Long) = playlistDao.playlistWithTvs(playlistId).map {
        tvs = it.tvs
        tvs
    }

    fun download(pos: Int) {
        addDisposable(
            updateTvUseCase.execute(UpdateTvParam(pos, tvs[pos].apply {
                download = true
            })).onIo()
        )
    }
}
