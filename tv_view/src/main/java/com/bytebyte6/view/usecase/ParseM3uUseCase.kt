package com.bytebyte6.view.usecase

import android.content.Context
import android.net.Uri
import com.bytebyte6.data.RxUseCase
import com.bytebyte6.data.dao.*
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.PlaylistTvCrossRef
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.UserPlaylistCrossRef
import com.bytebyte6.data.toTvs
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
        return Single.create { emitter ->
            val tvs = context.contentResolver.openInputStream(param)!!.toTvs()

            if (tvs.isEmpty()) {
                emitter.onError(FileNotFoundException("empty file!!!"))
                //不加return下面代码会接着执行
                return@create
            }

            val user = userDao.get()

            //1、创建播放列表 然后获取播放列表id 然后关联用户id
            val file = param.toString()
            playlistName = file.substring(file.indexOfLast { it == '/' }.plus(1))
            val playlistId = playlistDao.insert(Playlist(playlistName = playlistName))
            val userPlaylistCrossRef = UserPlaylistCrossRef(user.userId, playlistId)
            userPlaylistCrossRefDao.insert(userPlaylistCrossRef)

            //2、tv对象关联播放列表
            val tvIds = tvDao.insert(Tv.init(tvs))
            val playlistTvCrossRefs = mutableListOf<PlaylistTvCrossRef>()
            for (tvId in tvIds) {
                val playlistTvCrossRef = PlaylistTvCrossRef(playlistId, tvId)
                playlistTvCrossRefs.add(playlistTvCrossRef)
            }
            playlistCrossRefDao.insert(playlistTvCrossRefs)

            //3、返回结果
            emitter.onSuccess(playlistId)
        }
    }
}