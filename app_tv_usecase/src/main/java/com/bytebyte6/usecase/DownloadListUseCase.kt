package com.bytebyte6.usecase

import com.bytebyte6.base.RxUseCase
import com.bytebyte6.data.dao.TvDao
import com.google.android.exoplayer2.offline.DownloadManager

class DownloadListUseCase(
    private val tvDao: TvDao,
    private val downloadManager: DownloadManager
) : RxUseCase<Unit, List<TvAndDownload>>() {
    override fun run(param: Unit): List<TvAndDownload> {
        val list = mutableListOf<TvAndDownload>()

        val cursor = downloadManager.downloadIndex.getDownloads()

        while (cursor.moveToNext()) {
            val tvByUrl = tvDao.getTvByUrl(cursor.download.request.uri.toString())
            if (tvByUrl != null && tvByUrl.download) {
                list.add(TvAndDownload(tvByUrl, cursor.download))
            }
        }

        return list
    }
}