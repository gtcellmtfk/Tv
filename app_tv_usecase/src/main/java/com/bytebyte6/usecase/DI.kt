package com.bytebyte6.usecase

import com.bytebyte6.data.dao.UserDao
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val useCaseModule: Module = module {
    factory { CreateUserUseCase(get(UserDao::class)) }
    factory { UpdateTvUseCase(get()) }
    factory { SearchTvUseCase(get()) }
    factory { UpdateUserUseCase(get()) }
    factory { TvRefreshUseCase(get(), get()) }
    factory { DownloadListUseCase(get(), get()) }
    factory { CountryImageSearchUseCase(get(), get()) }
    factory { DeletePlaylistUseCase(get()/*, get()*/) }
    factory { TvLogoSearchUseCase(get(), get()) }
    factory {
        InitDataUseCase(
            get(),
            get(),
            get(),
            androidContext(),
            get()
        )
    }
    factory {
        ParseM3uUseCase(
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}