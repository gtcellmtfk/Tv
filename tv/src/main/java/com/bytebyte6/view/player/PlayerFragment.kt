package com.bytebyte6.view.player

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.bytebyte6.common.BaseShareFragment
import com.bytebyte6.common.NetworkHelper
import com.bytebyte6.common.showSnack
import com.bytebyte6.view.KEY_CACHE
import com.bytebyte6.view.KEY_VIDEO_URL
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.FragmentVideoBinding
import com.bytebyte6.viewmodel.PlayerViewModel
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

    private val networkHelper by inject<NetworkHelper>()

    private val httpDataSourceFactory by inject<HttpDataSource.Factory>()

    private var player: Player? = null

    private var request: DownloadRequest? = null

    private lateinit var url: String

    private val eventListener: Player.EventListener = object : Player.EventListener {

        private var playbackStateReady = false

        override fun onPlaybackStateChanged(state: Int) {
            playbackStateReady = state == Player.STATE_READY
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            requireActivity().runOnUiThread {
                binding?.progressBar?.isVisible = !isPlaying && !playbackStateReady
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            val tip = when (error.type) {
                ExoPlaybackException.TYPE_OUT_OF_MEMORY ->
                    getString(R.string.out_of_memory)
                ExoPlaybackException.TYPE_SOURCE ->
                    getString(R.string.source_error)
                ExoPlaybackException.TYPE_TIMEOUT ->
                    getString(R.string.timeout)
                else -> error.message.toString()
            }
            requireActivity().runOnUiThread {
                showSnack(requireView(), tip)
                binding?.progressBar?.isVisible = false
            }
        }
    }

    override fun initViewBinding(view: View): FragmentVideoBinding = FragmentVideoBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        request = requireArguments().getParcelable(KEY_CACHE) as DownloadRequest?
        url = requireArguments().getString(KEY_VIDEO_URL)!!
        networkHelper.networkIsCellular.observe(
            viewLifecycleOwner,
            Observer {
                if (it) {
                    showDialog()
                }
            })
        networkHelper.networkIsWifi.observe(
            viewLifecycleOwner,
            Observer {
                if (it) {
                    play()
                }
            })
    }

    private fun showDialog() {
        pause()
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.tip)
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
                requireActivity().finish()
            }
            .setPositiveButton(R.string.enter) { dialog, _ ->
                dialog.dismiss()
                play()
            }
            .setMessage(R.string.tip_play_confirm)
            .setCancelable(false)
            .create()
            .show()
    }

    private fun pause() {
        player?.pause()
    }

    override fun onResume() {
        super.onResume()
        if (networkHelper.networkIsWifi()) {
            play()
        }
    }

    override fun onStop() {
        super.onStop()
        stopPlay()
    }

    private fun play() {
        if (player != null) {
            player!!.play()
            return
        }
        player = if (url.isNotEmpty()) {
            getPlayer()
        } else {
            getLocalPlayer()
        }
        player!!.playWhenReady = true
        player!!.prepare()
        player!!.addListener(eventListener)
        binding?.apply {
            playerView.player = player
            playerView.keepScreenOn = true
            playerView.onResume()
        }
    }

    private fun getPlayer(): SimpleExoPlayer {
        val mediaItem = MediaItem.Builder()
            .setUri(url)
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()
        return SimpleExoPlayer.Builder(requireContext()).build().apply {
            setMediaItem(mediaItem)
        }
    }

    private fun getLocalPlayer(): SimpleExoPlayer {
        val cacheDataSourceFactory: DataSource.Factory = CacheDataSource.Factory()
            .setCache(downloadCache)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setCacheWriteDataSinkFactory(null)
        return SimpleExoPlayer.Builder(requireContext())
            .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
            .build().apply {
                setMediaItem(request!!.toMediaItem())
            }
    }

    private fun stopPlay() {
        binding?.apply {
            playerView.keepScreenOn = false
            playerView.onPause()
            playerView.player = null
            player?.removeListener(eventListener)
            player?.release()
            player = null
        }
    }
}