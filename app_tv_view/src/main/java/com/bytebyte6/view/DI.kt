package com.bytebyte6.view

import com.bytebyte6.data.dao.UserDao
import com.bytebyte6.view.home.HomeViewModel
import com.bytebyte6.view.me.MeViewModel
import com.bytebyte6.view.search.SearchViewModel
import com.bytebyte6.view.setting.UserViewModel
import com.bytebyte6.view.usecase.*
import com.bytebyte6.view.player.PlayerViewModel
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
    factory { UpdateUserUseCase(get()) }
    factory { TvRefreshUseCase(get(), get()) }
    factory { CountryImageSearchUseCase(get(), get()) }
    factory { DeletePlaylistUseCase(get(), get()) }
    factory { TvLogoSearchUseCase(get(), get()) }
    factory { InitDataUseCase(get(), get(), get(),androidApplication(),get()) }
    factory { ParseM3uUseCase(get(), get(), get(), get(), get(), get()) }
    viewModel { VideoListViewModel(get(),get()) }
    viewModel { PlayerViewModel() }
    viewModel { SearchViewModel(get(),get()) }
    viewModel { MeViewModel(get(), get(), get(),get(),get()) }
    viewModel { LauncherViewModel(get(),get()) }
    viewModel { HomeViewModel(get(), get(),get(),get()) }
    viewModel { UserViewModel(get(), get()) }
}