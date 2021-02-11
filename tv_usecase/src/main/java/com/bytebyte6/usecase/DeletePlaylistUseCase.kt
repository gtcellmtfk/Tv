package com.bytebyte6.usecase

import com.bytebyte6.common.RxUseCase
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.Tv

class DeletePlaylistUseCase(
    private val dataManager: DataManager
) : RxUseCase<List<Playlist>, Boolean>() {
    override fun run(param: List<Playlist>): Boolean {
        val tvs = mutableListOf<Tv>()
        param.forEach {
            tvs.addAll(dataManager.getPlaylistWithTvs(it.playlistId).tvs)
        }
        dataManager.deleteTv(tvs)
        dataManager.deletePlaylist(param)
        return true
    }
}