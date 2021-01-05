package com.bytebyte6.view

import com.bytebyte6.data.dao.UserDao
import com.bytebyte6.view.me.MeViewModel
import com.bytebyte6.view.search.SearchViewModel
import com.bytebyte6.view.usecase.CreateUserUseCase
import com.bytebyte6.view.usecase.ParseM3uUseCase
import com.bytebyte6.view.usecase.PlaylistByUserIdUseCase
import com.bytebyte6.view.video.VideoViewModel
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModule: Module = module {
    factory<Player> { SimpleExoPlayer.Builder(androidApplication()).build() }
    factory { CreateUserUseCase(get(UserDao::class)) }
    factory { PlaylistByUserIdUseCase(get(UserDao::class)) }
    factory { ParseM3uUseCase(get(), get(), get(), get(), get(), get()) }
    viewModel {
        TvViewModel(
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

    viewModel {
        MeViewModel(
            get(), get(), get(),get()
        )
    }
    viewModel {
        MainViewModel(
            get()
        )
    }
}