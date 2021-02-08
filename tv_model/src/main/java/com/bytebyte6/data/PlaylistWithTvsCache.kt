package com.bytebyte6.data

import android.util.LruCache
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.model.PageData

class PlaylistWithTvsCache(private val dataManager: DataManager) {
    private val playlistCache = LruCache<Long, PageData<Tv>>(10)

    @Synchronized
    fun getTvsByPlaylistId(playlistId: Long, page: Int): List<Tv> {
        return pageData(playlistId).paging(page)
    }

    @Synchronized
    fun updateTvsByPlaylistId(playlistId: Long, newList: List<Tv>, page: Int) {
        val pageData = playlistCache[playlistId]
        if (pageData == null) {
            throw IllegalStateException(
                "Please call getTVCountByPlayListId or " +
                        "getVsByPlayListId first, otherwise the cached data cannot be updated"
            )
        } else {
            pageData.updatePaging(page, newList)
        }
    }

    @Synchronized
    fun getTvCountByPlaylistId(playlistId: Long): Int {
        return pageData(playlistId).count
    }

    private fun pageData(playlistId: Long): PageData<Tv> {
        var pageData = playlistCache[playlistId]
        if (pageData == null) {
            val playlistWithTvs = dataManager.getPlaylistWithTvs(playlistId)
            pageData = PageData(PAGE_SIZE, playlistWithTvs.tvs)
            playlistCache.put(playlistId, pageData)
        }
        return pageData
    }
}