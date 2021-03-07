package com.bytebyte6.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bytebyte6.common.BaseViewModel
import com.bytebyte6.common.Event
import com.bytebyte6.common.NetworkHelper
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.User
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.util.MimeTypes

@SuppressLint("StaticFieldLeak")
class PlayerViewModel(
    private val networkHelper: NetworkHelper,
    dataManager: DataManager,
    private val downloadCache: Cache,
    private val httpDataSourceFactory: HttpDataSource.Factory,
    private val context: Context
) : BaseViewModel() {

    private val user = dataManager.user()

    private val _play = MutableLiveData<Player>()
    val play: LiveData<Player> = _play

    private val _showMobileDialog = MutableLiveData<Event<Unit>>()
    val showMobileDialog: LiveData<Event<Unit>> = _showMobileDialog

    private val _showNoneDialog = MutableLiveData<Event<Unit>>()
    val showNoneDialog: LiveData<Event<Unit>> = _showNoneDialog

    private val networkType = networkHelper.networkType

    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean> = _showProgressBar

    private val _onPlayerError = MutableLiveData<Event<ExoPlaybackException>>()
    val onPlayerError: LiveData<Event<ExoPlaybackException>> = _onPlayerError

    private var player: Player? = null

    var url: String = ""

    var downloadRequest: DownloadRequest? = null

    private var onlyWifiPlay: Boolean = true

    private val eventListener: Player.EventListener = object : Player.EventListener {

        private var playbackStateReady = false

        override fun onPlaybackStateChanged(state: Int) {
            playbackStateReady = state == Player.STATE_READY
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _showProgressBar.postValue(!isPlaying && !playbackStateReady)
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            if (isBehindLiveWindow(error)) {
                player?.seekToDefaultPosition()
                player?.prepare()
                return
            }
//            if (error.cause is BehindLiveWindowException) {
//                retry()
//                return
//            }
            if (error.cause is HttpDataSource.InvalidResponseCodeException) {
                retry()
                return
            }
            _showProgressBar.postValue(false)
            _onPlayerError.postValue(Event(error))
        }
    }

    private val networkTypeObserver = Observer<NetworkHelper.NetworkType> { type ->
        when (type) {
            NetworkHelper.NetworkType.WIFI -> playOrInit()
            NetworkHelper.NetworkType.MOBILE -> {
                if (onlyWifiPlay) {
                    // Dialog展示的三个条件
                    // 当前网络为数据
                    // 用户设置只有Wifi状态下播放
                    // 播放缓存除外
                    if (url.isNotEmpty()) {
                        _showMobileDialog.postValue(Event(Unit))
                    }
                } else {
                    playOrInit()
                }
            }
            else -> _showNoneDialog.postValue(Event(Unit))
        }
    }

    fun retry() {
        _showProgressBar.postValue(true)
        destroyPlayer()
        initPlayer()
    }

    fun playOrInit() {
        if (player == null) {
            initPlayer()
        } else {
            player!!.addListener(eventListener)
            player!!.play()
            _play.postValue(player!!)
        }
    }

    fun onResume() {
        _showProgressBar.postValue(true)
        user.observeForever(object : Observer<User> {
            override fun onChanged(user1: User) {
                user.removeObserver(this)
                onlyWifiPlay = user1.playOnlyOnWifi
                networkType.observeForever(networkTypeObserver)
            }
        })
    }

    fun onStop() {
        networkType.removeObserver(networkTypeObserver)
        player?.pause()
        player?.removeListener(eventListener)
    }

    private fun destroyPlayer() {
        player?.removeListener(eventListener)
        player?.release()
        player = null
    }

    private fun initPlayer() {
        player = if (url.isEmpty()) {
            getCachePlayer()
        } else {
            getPlayer()
        }.apply {
            addListener(eventListener)
            prepare()
            play()
            _play.postValue(this)
        }
    }

    private fun getPlayer(): SimpleExoPlayer {
        val mediaItem = MediaItem.Builder()
            .setUri(url)
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()
        return SimpleExoPlayer.Builder(context).build().apply {
            setMediaItem(mediaItem)
        }
    }

    private fun getCachePlayer(): SimpleExoPlayer {
        val cacheDataSourceFactory: DataSource.Factory = CacheDataSource.Factory()
            .setCache(downloadCache)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setCacheWriteDataSinkFactory(null)
        return SimpleExoPlayer.Builder(context)
            .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
            .build().apply {
                setMediaItem(downloadRequest!!.toMediaItem())
            }
    }

    fun networkIsConnected() = networkHelper.networkIsConnected()

    private fun isBehindLiveWindow(e: ExoPlaybackException): Boolean {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false
        }
        var cause: Throwable? = e.sourceException
        while (cause != null) {
            if (cause is BehindLiveWindowException) {
                return true
            }
            cause = cause.cause
        }
        return false
    }

    override fun onCleared() {
        super.onCleared()
        destroyPlayer()
    }
}