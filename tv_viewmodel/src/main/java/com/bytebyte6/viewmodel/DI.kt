package com.bytebyte6.viewmodel

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModule: Module = module {
    viewModel {
        VideoListViewModel(
            get(),
            get(),
            get()
        )
    }
    viewModel { PlayerViewModel(get(), get(), get(), get(), get()) }
    viewModel {
        SearchViewModel2(
            get(),
            get(),
            get()
        )
    }
    viewModel { MeViewModel(get(), get(), get()) }
    viewModel {
        PlaylistViewModel(
            get(),
            get(),
            get()
        )
    }
    viewModel { LauncherViewModel(get()) }
    viewModel {
        HomeViewModel(
            get(),
            get(),
            get()
        )
    }
    viewModel { UserViewModel(get(), get()) }
    viewModel { FavoriteViewModel(get(), get()) }
    viewModel { DownloadViewModel(get(), get(), get()) }
}