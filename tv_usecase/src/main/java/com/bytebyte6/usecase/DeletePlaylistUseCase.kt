package com.bytebyte6.usecase

import com.bytebyte6.common.RxUseCase
import com.bytebyte6.data.dao.PlaylistDao
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.Tv

class DeletePlaylistUseCase(
    private val playlistDao: PlaylistDao
) : RxUseCase<List<Playlist>, Boolean>() {
    override fun run(param: List<Playlist>): Boolean {
        val tvs = mutableListOf<Tv>()
        param.forEach {
            tvs.addAll(
                playlistDao.getPlaylistWithTvsById(it.playlistId).tvs
            )
        }
        playlistDao.delete(param)
        return true
    }
}