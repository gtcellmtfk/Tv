package com.bytebyte6.view.usecase

import android.content.Context
import android.net.Uri
import com.bytebyte6.data.RxUseCase
import com.bytebyte6.data.dao.*
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.PlaylistTvCrossRef
import com.bytebyte6.data.entity.UserPlaylistCrossRef
import com.bytebyte6.data.toTvs
import com.bytebyte6.view.R
import io.reactivex.rxjava3.core.Single
import java.io.FileNotFoundException

class ParseM3uUseCase(
    private val tvDao: TvDao,
    private val userDao: UserDao,
    private val userPlaylistCrossRefDao: UserPlaylistCrossRefDao,
    private val playlistCrossRefDao: PlaylistTvCrossRefDao,
    private val playlistDao: PlaylistDao,
    private val context: Context
) : RxUseCase<Uri, Long>() {
    var playlistName: String = ""
    override fun getSingle(param: Uri): Single<Long> {
        return Single.create {
            val tvs = context.contentResolver.openInputStream(param)!!.toTvs()

            if (tvs.isEmpty()) {
                it.onError(FileNotFoundException("empty file!!!"))
                //不加return下面代码会接着执行
                return@create
            }

            val user = userDao.get()

            //1、创建播放列表 然后获取播放列表id 然后关联用户id
            playlistName = context.getString(R.string.me_playlist).plus(playlistDao.count())
            val playlistId = playlistDao.insert(Playlist(playlistName = playlistName))
            val userPlaylistCrossRef = UserPlaylistCrossRef(user.userId, playlistId)
            userPlaylistCrossRefDao.insert(userPlaylistCrossRef)

            //2、tv对象关联播放列表
            val tvIds = tvDao.insertAll(tvs)
            val playlistTvCrossRefs = mutableListOf<PlaylistTvCrossRef>()
            for (tvId in tvIds) {
                val playlistTvCrossRef = PlaylistTvCrossRef(playlistId, tvId)
                playlistTvCrossRefs.add(playlistTvCrossRef)
            }
            playlistCrossRefDao.insertAll(playlistTvCrossRefs)

            //3、返回结果
            it.onSuccess(playlistId)
        }
    }
}