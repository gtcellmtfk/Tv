package com.bytebyte6.view.usecase

import com.bytebyte6.data.RxSingleUseCase
import com.bytebyte6.data.dao.PlaylistDao
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.Tv
import io.reactivex.rxjava3.core.Single

class DeletePlaylistUseCase(
    private val playlistDao: PlaylistDao,
    private val tvDao: TvDao
) : RxSingleUseCase<List<Playlist>, Boolean>() {
    override fun getSingle(param: List<Playlist>): Single<Boolean> {
        return Single.create { emitter ->
            val tvs = mutableListOf<Tv>()
            param.forEach {
                tvs.addAll(
                    playlistDao.getPlaylistWithTvsById(it.playlistId).tvs
                )
            }
            tvDao.delete(tvs)
            playlistDao.delete(param)
            emitter.onSuccess(true)
        }
    }
}