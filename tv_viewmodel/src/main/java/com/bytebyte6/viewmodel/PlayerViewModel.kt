package com.bytebyte6.viewmodel

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
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.util.MimeTypes

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
            _onPlayerError.postValue(Event(error))
            _showProgressBar.postValue(false)
        }
    }

    private val networkTypeObserver = Observer<NetworkHelper.NetworkType> { type ->
        emit(type)
    }

    private fun emit(type: NetworkHelper.NetworkType?) {
        when (type) {
            NetworkHelper.NetworkType.WIFI -> playThatShit()
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
                    playThatShit()
                }
            }
            else -> _showNoneDialog.postValue(Event(Unit))
        }
    }

    fun retry() {
        initPlayer()
    }

    fun playThatShit() {
        if (player == null) {
            initPlayer()
        } else {
            player!!.play()
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
        destroyPlayer()
    }

    private fun destroyPlayer() {
        player?.removeListener(eventListener)
        player?.release()
        player = null
    }

    private fun initPlayer() {
        destroyPlayer()
        player = if (url.isEmpty()) {
            getCachePlayer()
        } else {
            getPlayer()
        }.apply {
            playWhenReady = true
            prepare()
            addListener(eventListener)
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
}