package com.bytebyte6.view.download

import android.app.Notification
import android.content.Context
import android.net.Uri
import com.bytebyte6.view.R
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.PlatformScheduler
import com.google.android.exoplayer2.scheduler.Scheduler
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import org.koin.android.ext.android.inject

class RtmpDownloadService : DownloadService(ID) {
    companion object {
        const val ID = 1
        const val JOB_ID = 1
        const val CHANNEL_ID = "CHANNEL_ID"

        /**
         * 添加单个
         */
        fun addDownload(context: Context, url: String) {
            val downloadRequest = DownloadRequest.Builder(url, Uri.parse(url)).build()
            sendAddDownload(context, RtmpDownloadService::class.java, downloadRequest, true)
        }

        /**
         * 删除单个
         */
        fun removeDownload(context: Context, url: String) {
            sendRemoveDownload(context, RtmpDownloadService::class.java, url, true)
        }

        /**
         * 开始下载全部
         */
        fun resumeDownloads(context: Context){
            sendResumeDownloads(context,RtmpDownloadService::class.java,true)
        }

        /**
         * 暂停下载全部
         */
        fun pauseDownloads(context: Context){
            sendPauseDownloads(context,RtmpDownloadService::class.java,true)
        }

        /**
         * 删除所有
         */
        fun removeAllDownload(context: Context) {
            sendRemoveAllDownloads(context, RtmpDownloadService::class.java, true)
        }
    }

    private val manager by inject<DownloadManager>()

    private val downloadNotificationHelper by lazy {
        DownloadNotificationHelper(
            this,
            CHANNEL_ID
        )
    }

    override fun getDownloadManager(): DownloadManager {
        return manager
    }

    override fun getForegroundNotification(downloads: MutableList<Download>): Notification {
        return downloadNotificationHelper.buildProgressNotification(
            this,
            R.mipmap.ic_launcher,
            null,
            null,
            downloads
        )
    }

    override fun getScheduler(): Scheduler {
        return PlatformScheduler(
            this,
            JOB_ID
        )
    }
}