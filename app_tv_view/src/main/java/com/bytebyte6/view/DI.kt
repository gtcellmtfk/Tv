package com.bytebyte6.view

import com.bytebyte6.data.dao.UserDao
import com.bytebyte6.usecase.*
import com.bytebyte6.view.download.DownloadViewModel
import com.bytebyte6.view.home.HomeViewModel
import com.bytebyte6.view.me.MeViewModel
import com.bytebyte6.view.me.PlaylistViewModel
import com.bytebyte6.view.search.SearchViewModel
import com.bytebyte6.view.setting.UserViewModel
import com.bytebyte6.view.player.PlayerViewModel
import com.bytebyte6.view.videolist.VideoListViewModel
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModule: Module = module {
    viewModel { VideoListViewModel(get(),get(),get()) }
    viewModel { PlayerViewModel() }
    viewModel { SearchViewModel(get(),get(),get()) }
    viewModel { MeViewModel(get(), get(), get(),get(),get()) }
    viewModel { PlaylistViewModel(get(), get(), get()) }
    viewModel { LauncherViewModel(get(),get()) }
    viewModel { HomeViewModel(get(), get(),get(),get()) }
    viewModel { UserViewModel(get(), get()) }
    viewModel { FavoriteViewModel( get()) }
    viewModel { DownloadViewModel( get(),get()) }
}