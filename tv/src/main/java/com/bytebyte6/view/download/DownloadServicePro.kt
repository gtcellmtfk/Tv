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

class DownloadServicePro : DownloadService(
    ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    CHANNEL_ID,
    R.string.download,
    0
) {
    companion object {
        const val ID = 1
        const val JOB_ID = 1
        const val CHANNEL_ID = "CHANNEL_ID"
        const val FOREGROUND = true

        /**
         * 添加单个
         */
        fun addDownload(context: Context, url: String) {
            val downloadRequest = DownloadRequest.Builder(url, Uri.parse(url)).build()
            sendAddDownload(context, DownloadServicePro::class.java, downloadRequest, FOREGROUND)
        }

        /**
         * 删除单个
         */
        fun removeDownload(context: Context, url: String) {
            sendRemoveDownload(context, DownloadServicePro::class.java, url, FOREGROUND)
        }

        /**
         * 开始下载全部
         */
        fun resumeDownloads(context: Context){
            sendResumeDownloads(context, DownloadServicePro::class.java, FOREGROUND)
        }

        /**
         * 暂停下载全部
         */
        fun pauseDownloads(context: Context){
            sendPauseDownloads(context, DownloadServicePro::class.java, FOREGROUND)
        }

        /**
         * 删除所有
         */
        fun removeAllDownload(context: Context) {
            sendRemoveAllDownloads(context, DownloadServicePro::class.java, FOREGROUND)
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
            R.drawable.ic_favorite,
            null,
            getString(R.string.tip_working),
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