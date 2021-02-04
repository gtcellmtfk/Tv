package com.bytebyte6.usecase

import android.content.Context
import android.net.Uri
import com.bytebyte6.common.RxUseCase
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.PlaylistTvCrossRef
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.UserPlaylistCrossRef
import com.bytebyte6.data.model.Language
import com.bytebyte6.data.toTvs
import org.jetbrains.annotations.TestOnly

class ParseM3uUseCase(
    private val dataManager: DataManager,
    private val context: Context
) : RxUseCase<Uri, Playlist>() {

    private var tvsFromFile: List<Tv>? = null

    @TestOnly
    fun setTvs(list:List<Tv>){
        tvsFromFile=list
    }

    override fun run(param: Uri): Playlist {

        if (tvsFromFile == null) {
            tvsFromFile = context.contentResolver.openInputStream(param)!!.toTvs()
        }

        val tvsFromDb = mutableListOf<Tv>()

        val inserts = tvsFromFile!!.filter {
            val tvFromDb = dataManager.getTvByUrl(it.url)
            if (tvFromDb == null) {
                true
            } else {
                tvsFromDb.add(tvFromDb)
                false
            }
        }.map {
            if (it.category.isEmpty()) {
                it.category = "Other"
            }
            if (it.language.isEmpty()) {
                it.language = mutableListOf(Language("Other", "777"))
            }
            it
        }

        val insertsWithIds = dataManager.insertTv(inserts).mapIndexed { index, id ->
            inserts[index].tvId = id
            inserts[index]
        }

        val tvs = insertsWithIds.plus(tvsFromDb)

        val user = dataManager.getUser()

        //1、创建播放列表 然后获取播放列表id 然后关联用户id
        val fileUri = param.toString()
        val playlistName = fileUri.substring(fileUri.indexOfLast { it == '/' }.plus(1))
        val playlistId = dataManager.insertPlaylist(Playlist(playlistName = playlistName))
        val userPlaylistCrossRef = UserPlaylistCrossRef(user.userId, playlistId)
        dataManager.crossRefUserWithPlaylist(userPlaylistCrossRef)

        //2、tv对象关联播放列表
        val playlistTvCrossRefs = mutableListOf<PlaylistTvCrossRef>()
        for (tv in tvs) {
            val playlistTvCrossRef = PlaylistTvCrossRef(playlistId, tv.tvId)
            playlistTvCrossRefs.add(playlistTvCrossRef)
        }
        dataManager.crossRefPlaylistWithTv(playlistTvCrossRefs)

        //3、返回结果
        return Playlist(playlistId, playlistName)
    }
}