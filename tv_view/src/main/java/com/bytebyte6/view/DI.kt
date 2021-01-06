package com.bytebyte6.view

import com.bytebyte6.data.dao.UserDao
import com.bytebyte6.view.home.HomeViewModel
import com.bytebyte6.view.me.MeViewModel
import com.bytebyte6.view.search.SearchViewModel
import com.bytebyte6.view.usecase.*
import com.bytebyte6.view.video.VideoViewModel
import com.bytebyte6.view.videolist.VideoListViewModel
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModule: Module = module {
    factory<Player> { SimpleExoPlayer.Builder(androidApplication()).build() }
    factory { CreateUserUseCase(get(UserDao::class)) }
    factory { SearchTvUseCase(get()) }
    factory { CallTvApiUseCase(get(), get()) }
    factory { AppInitUseCase(get(), get(), get(), androidApplication()) }
    factory { PlaylistByUserIdUseCase(get(UserDao::class)) }
    factory { ParseM3uUseCase(get(), get(), get(), get(), get(), get()) }
    viewModel { VideoListViewModel(get()) }
    viewModel { VideoViewModel() }
    viewModel { SearchViewModel(get()) }
    viewModel { MeViewModel(get(), get(), get()) }
    viewModel { LauncherViewModel(get(),get()) }
    viewModel { HomeViewModel(get(), get()) }
}