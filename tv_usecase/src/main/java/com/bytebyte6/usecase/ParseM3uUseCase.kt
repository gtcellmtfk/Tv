package com.bytebyte6.usecase

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.bytebyte6.common.RxUseCase
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.M3u
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.PlaylistTvCrossRef
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.UserPlaylistCrossRef
import org.jetbrains.annotations.TestOnly

class ParseM3uUseCase(
    private val dataManager: DataManager,
    private val context: Context? = null
) : RxUseCase<Uri, Playlist>() {

    private var forTest: List<Tv>? = null

    @TestOnly
    fun setTvs(list: List<Tv>) {
        forTest = list
    }

    override fun run(param: Uri): Playlist {

        val support = param.path!!.endsWith(".m3u")
                || param.path!!.endsWith(".m3u8")
                || param.path!!.endsWith(".txt")
        if (!support) {
            throw UnsupportedOperationException("only support .m3u or .m3u8 or .txt file!")
        }

        var playlistName = ""
        context?.let { context1 ->
            context1.contentResolver.query(param, null, null, null, null)
                ?.use {
                    val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    it.moveToFirst()
                    playlistName = it.getString(nameIndex)
                }
        }
        if (playlistName.isEmpty()) {
            val fileUri = param.toString()
            playlistName = fileUri.substring(fileUri.indexOfLast { it == '/' }.plus(1))
        }

        val tvsFromUri =
            if (forTest == null)
                M3u.getTvs(context!!.contentResolver.openInputStream(param)!!)
            else
                forTest

        val tvsFromDb = mutableListOf<Tv>()

        val needInserts = tvsFromUri!!.filter {
            val tvFromDb = dataManager.getTvByUrl(it.url)
            if (tvFromDb == null) {
                true
            } else {
                tvsFromDb.add(tvFromDb)
                false
            }
        }.map {
            val country = dataManager.getCountryByCode(it.country.code)
            Tv.init(it, country)
        }

        val insertsWithIds = dataManager.insertTv(needInserts).mapIndexed { index, id ->
            needInserts[index].tvId = id
            needInserts[index]
        }

        val allTvs = insertsWithIds.plus(tvsFromDb)

        val user = dataManager.getCurrentUserIfNotExistCreate()

        //1、创建播放列表 然后获取播放列表id 然后关联用户id
        val playlistId = dataManager.insertPlaylist(Playlist(playlistName = playlistName))
        val userPlaylistCrossRef = UserPlaylistCrossRef(user.userId, playlistId)
        dataManager.crossRefUserWithPlaylist(userPlaylistCrossRef)

        //2、tv对象关联播放列表
        val playlistTvCrossRefs = mutableListOf<PlaylistTvCrossRef>()
        for (tv in allTvs) {
            val playlistTvCrossRef = PlaylistTvCrossRef(playlistId, tv.tvId)
            playlistTvCrossRefs.add(playlistTvCrossRef)
        }
        dataManager.crossRefPlaylistWithTv(playlistTvCrossRefs)

        //3、返回结果
        return Playlist(playlistId, playlistName)
    }
}
