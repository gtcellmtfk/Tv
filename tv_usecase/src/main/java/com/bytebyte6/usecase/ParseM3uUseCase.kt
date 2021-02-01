package com.bytebyte6.usecase

import android.content.Context
import android.net.Uri
import com.bytebyte6.common.RxUseCase
import com.bytebyte6.data.dao.*
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.PlaylistTvCrossRef
import com.bytebyte6.data.entity.UserPlaylistCrossRef
import com.bytebyte6.data.model.Language
import com.bytebyte6.data.toTvs

class ParseM3uUseCase(
    private val tvDao: TvDao,
    private val userDao: UserDao,
    private val userPlaylistCrossRefDao: UserPlaylistCrossRefDao,
    private val playlistCrossRefDao: PlaylistTvCrossRefDao,
    private val playlistDao: PlaylistDao,
    private val context: Context
) : RxUseCase<Uri, Playlist>() {

    override fun run(param: Uri): Playlist {
        val tvsFromFile = context.contentResolver.openInputStream(param)!!.toTvs()

        val inserts = tvsFromFile.filter {
            val tvFromDb = tvDao.getTvByUrl(it.url)
            tvFromDb == null
        }.map {
            if (it.category.isEmpty()) {
                it.category = "Other"
            }
            if (it.language.isEmpty()) {
                it.language = mutableListOf(Language("Other", "777"))
            }
            it
        }

        val insertsWithIds = tvDao.insert(inserts).mapIndexed { index, id ->
            inserts[index].tvId = id
            inserts[index]
        }

        val tvsFromDb = tvsFromFile.filter {
            val tvFromDb = tvDao.getTvByUrl(it.url)
            if (tvFromDb != null) {
                it.tvId = tvFromDb.tvId
                true
            } else false
        }

        val tvs = insertsWithIds.plus(tvsFromDb)

//        val tvs: List<Tv> = tvsFromFile.map {
//            val tvFromDb = tvDao.getTvByUrl(it.url)
//            if (tvFromDb == null) {
//                if (it.category.isEmpty()) {
//                    it.category = "Other"
//                }
//                if (it.language.isEmpty()) {
//                    it.language = mutableListOf(Language("Other", "777"))
//                }
//                it.tvId = tvDao.insert(it)
//                it
//            } else {
//                tvFromDb
//            }
//        }

        val user = userDao.getUser()

        //1、创建播放列表 然后获取播放列表id 然后关联用户id
        val fileUri = param.toString()
        val playlistName = fileUri.substring(fileUri.indexOfLast { it == '/' }.plus(1))
        val playlistId = playlistDao.insert(Playlist(playlistName = playlistName))
        val userPlaylistCrossRef = UserPlaylistCrossRef(user.userId, playlistId)
        userPlaylistCrossRefDao.insert(userPlaylistCrossRef)

        //2、tv对象关联播放列表
        val playlistTvCrossRefs = mutableListOf<PlaylistTvCrossRef>()
        for (tv in tvs) {
            val playlistTvCrossRef = PlaylistTvCrossRef(playlistId, tv.tvId)
            playlistTvCrossRefs.add(playlistTvCrossRef)
        }
        playlistCrossRefDao.insert(playlistTvCrossRefs)

        //3、返回结果
        return Playlist(playlistId, playlistName)
    }
}