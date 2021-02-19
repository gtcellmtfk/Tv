package com.bytebyte6.usecase

import com.bytebyte6.image.SearchImage
import com.bytebyte6.image.SearchImageImpl
import com.bytebyte6.usecase.work.AppDelegatingWorkerFactory
import com.bytebyte6.usecase.work.SearchCountryImage
import com.bytebyte6.usecase.work.SearchTvLogo
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File
import java.util.concurrent.Executors

val exoPlayerModule = module {
    single<HttpDataSource.Factory> { DefaultHttpDataSourceFactory() }
    single {
        DownloadManager(
            androidContext(),
            get(DatabaseProvider::class.java),
            get(Cache::class.java),
            get(HttpDataSource.Factory::class.java),
            Executors.newFixedThreadPool(5)
        )
    }
    single<DatabaseProvider> { ExoDatabaseProvider(androidContext()) }
    single<Cache> {
        SimpleCache(
            File(androidContext().cacheDir, "video"),
            NoOpCacheEvictor(),
            get(DatabaseProvider::class.java)
        )
    }
}

val useCaseModule: Module = module {
    factory { FavoriteTvUseCase(get()) }
    factory { UpdateUserUseCase(get()) }
    factory { DownloadListUseCase(get(), get()) }
    factory { ChangeCountryImageUseCase(get(), get()) }
    factory { DeletePlaylistUseCase(get()) }
    factory<SearchTvLogoUseCase> { SearchTvLogoUseCaseImpl(get(), get()) }
    factory<InitAppUseCase> { InitAppUseCaseImpl(get(), androidContext(), get()) }
    factory { ParseM3uUseCase(get(), get()) }

    /**图片搜索*/
    single { AppDelegatingWorkerFactory(get(), get()) }
    single { SearchCountryImage(get(), get()) }
    single { SearchTvLogo(get(), get()) }
    factory<SearchImage> { SearchImageImpl() }
}