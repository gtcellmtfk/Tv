package com.bytebyte6.view.player

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.bytebyte6.app_tv_viewmodel.PlayerViewModel
import com.bytebyte6.base.logd
import com.bytebyte6.base.BaseShareFragment
import com.bytebyte6.base.Message
import com.bytebyte6.base.showSnack
import com.bytebyte6.view.KEY_CACHE
import com.bytebyte6.view.KEY_VIDEO_URL
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.FragmentVideoBinding
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
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class PlayerFragment :
    BaseShareFragment<FragmentVideoBinding>(R.layout.fragment_video) {

    companion object {
        const val TAG: String = "PlayerFragment"
        fun newInstance(bundle: Bundle) = PlayerFragment().apply {
            arguments = bundle
        }
    }

    private val viewModel by viewModel<PlayerViewModel>()

    private val downloadCache by inject<Cache>()

    private val httpDataSourceFactory by inject<HttpDataSource.Factory>()

    private var player: Player? = null

    override fun initViewBinding(view: View): FragmentVideoBinding = FragmentVideoBinding.bind(view)

    override fun onResume() {
        super.onResume()
        start()
    }

    override fun onStop() {
        super.onStop()
        stop()
    }

    private val listener: Player.EventListener = object : Player.EventListener {

        private var playbackStateReady = false

        override fun onPlaybackStateChanged(state: Int) {
            playbackStateReady = state == Player.STATE_READY
        }

        override fun onIsLoadingChanged(isLoading: Boolean) {
            super.onIsLoadingChanged(isLoading)
            logd("onIsLoadingChanged $isLoading")
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            requireActivity().runOnUiThread {
                binding?.progressBar?.isVisible = !isPlaying && !playbackStateReady
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            val m = if (error.message != null) {
                Message(
                    actionStringId = R.string.tip_play_retry,
                    message = error.message!!,
                    longDuration = true
                )
            } else {
                Message(
                    actionStringId = R.string.tip_play_retry,
                    message = getString(R.string.tip_play_error),
                    longDuration = true
                )
            }
            requireActivity()
                .runOnUiThread {
                    showSnack(requireView(), m)
                }
        }
    }

    private fun start() {
        binding?.apply {
            val request = requireArguments().getParcelable(KEY_CACHE) as DownloadRequest?
            val url = requireArguments().getString(KEY_VIDEO_URL)!!
            logd("url=$url")
            if (url.isNotEmpty()) {
                player = SimpleExoPlayer.Builder(requireContext()).build()
                val mediaItem = MediaItem.Builder()
                    .setUri(url)
                    .setMimeType(MimeTypes.APPLICATION_M3U8)
                    .build()
                player!!.setMediaItem(mediaItem)
            } else {
                player = getLocalPlayer()
                player!!.setMediaItem(request!!.toMediaItem())
            }
            player!!.playWhenReady = true
            player!!.prepare()
            player!!.addListener(listener)
            playerView.player = player
            playerView.keepScreenOn = true
            playerView.onResume()
        }
    }

    private fun getLocalPlayer(): SimpleExoPlayer {
        val cacheDataSourceFactory: DataSource.Factory = CacheDataSource.Factory()
            .setCache(downloadCache)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setCacheWriteDataSinkFactory(null)
        return SimpleExoPlayer.Builder(requireContext())
            .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
            .build()
    }

    private fun stop() {
        binding?.apply {
            playerView.keepScreenOn = false
            playerView.onPause()
            player?.removeListener(listener)
            player?.release()
            playerView.player = null
            player = null
        }
    }
}