package com.bytebyte6.usecase

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.annotation.Keep
import com.bytebyte6.common.RxUseCase
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.M3u
import com.bytebyte6.data.entity.Playlist
import com.bytebyte6.data.entity.PlaylistTvCrossRef
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.UserPlaylistCrossRef


@Keep
data class ParseParam(
        val uri: Uri? = null,
        val assetsFileName: String? = null,
        val forTest: List<Tv>? = null
)

class ParseM3uUseCase(
        private val dataManager: DataManager,
        private val context: Context? = null
) : RxUseCase<ParseParam, Playlist>() {

    override fun run(param: ParseParam): Playlist {

        val playlistName = getPlaylistName(param)

        val needInserts = getTvs(param).map {
            val country = dataManager.getCountryByCode(it.countryCode)
            it.countryId = country.countryId
            it.countryName = country.name
            it
        }

        val insertsWithIds = dataManager.insertTv(needInserts).mapIndexed { index, id ->
            needInserts[index].tvId = id
            needInserts[index]
        }

        //创建播放列表 然后获取播放列表id 然后关联用户id
        val user = dataManager.getCurrentUserIfNotExistCreate()
        val playlist = Playlist(playlistName = playlistName, total = insertsWithIds.size)
        val playlistId = dataManager.insertPlaylist(playlist)
        val userPlaylistCrossRef = UserPlaylistCrossRef(user.userId, playlistId)
        dataManager.crossRefUserWithPlaylist(userPlaylistCrossRef)

        //tv对象关联播放列表
        val playlistTvCrossRefs = mutableListOf<PlaylistTvCrossRef>()
        for (tv in insertsWithIds) {
            val playlistTvCrossRef = PlaylistTvCrossRef(playlistId, tv.tvId)
            playlistTvCrossRefs.add(playlistTvCrossRef)
        }
        dataManager.crossRefPlaylistWithTv(playlistTvCrossRefs)

        return Playlist(playlistId, playlistName)
    }

    private fun getPlaylistName(param: ParseParam): String {
        return when {
            param.uri != null -> {
                var playlistName = ""
                context!!.contentResolver.query(param.uri, null, null, null, null)
                        ?.use {
                            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            it.moveToFirst()
                            playlistName = it.getString(nameIndex)
                        }
                if (playlistName.isEmpty()) {
                    val fileUri = param.uri.path!!
                    playlistName = fileUri.substring(fileUri.indexOfLast { it == '/' }.plus(1))
                }
                playlistName
            }
            param.assetsFileName != null -> {
                param.assetsFileName
            }
            else -> {
                "forTest.m3u"
            }
        }
    }

    private fun getTvs(param: ParseParam): List<Tv> {
        return param.forTest
                ?: if (param.assetsFileName != null) {
                    val inputStream = context!!.assets.open(param.assetsFileName)
                    M3u.getTvs(inputStream)
                } else {
                    val support = param.uri!!.path!!.endsWith(".m3u")
                            || param.uri.path!!.endsWith(".m3u8")
                            || param.uri.path!!.endsWith(".txt")
                    if (!support) {
                        throw UnsupportedOperationException("only support .m3u or .m3u8 or .txt file!")
                    }
                    M3u.getTvs(context!!.contentResolver.openInputStream(param.uri)!!)
                }
    }
}
