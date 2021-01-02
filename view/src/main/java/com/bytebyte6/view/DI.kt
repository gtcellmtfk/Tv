package com.bytebyte6.view

import com.bytebyte6.view.search.SearchViewModel
import com.bytebyte6.view.video.VideoViewModel
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModule:Module= module {
    factory <Player> { SimpleExoPlayer.Builder(androidApplication()).build() }
    viewModel {
        IpTvViewModel(
            get(),
            get()
        )
    }
    viewModel {
        VideoViewModel(
            get()
        )
    }
    viewModel {
        SearchViewModel(
            get(),
            get()
        )
    }
}