package com.bytebyte6.usecase

import android.content.Context
import com.bytebyte6.data.dao.UserDao
import com.bytebyte6.image.SearchImage
import com.bytebyte6.image.SearchImageImpl
import com.bytebyte6.usecase.work.AppDelegatingWorkerFactory
import com.bytebyte6.usecase.work.CountryImageSearch
import com.bytebyte6.usecase.work.TvLogoSearch
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
import java.util.concurrent.Executors

val useCaseModule: Module = module {
    factory { CreateUserUseCase(get(UserDao::class)) }
    factory { UpdateTvUseCase(get()) }
    factory { SearchTvUseCase(get()) }
    factory { UpdateUserUseCase(get()) }
    factory { TvRefreshUseCase(get(), get()) }
    factory { DownloadListUseCase(get(), get()) }
    factory { CountryImageSearchUseCase(get(), get()) }
    factory { DeletePlaylistUseCase(get()) }
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

    /**图片搜索*/
    single { AppDelegatingWorkerFactory(get(), get()) }
    single { CountryImageSearch(get(), get()) }
    single { TvLogoSearch(get(), get()) }
    factory<SearchImage> { SearchImageImpl() }

    /**Video*/
    single<HttpDataSource.Factory> { DefaultHttpDataSourceFactory() }
    single {
        DownloadManager(
            androidContext(),
            get(DatabaseProvider::class.java),
            get(),
            get(HttpDataSource.Factory::class.java),
            Executors.newFixedThreadPool(5)
        )
    }
    single<DatabaseProvider> { ExoDatabaseProvider(androidContext()) }
    single<Cache> {
        SimpleCache(
            androidContext().cacheDir,
            NoOpCacheEvictor(),
            get(DatabaseProvider::class.java)
        )
    }
}

//private fun getDownloadManager(context: Context): DownloadManager {
//    val exoDatabaseProvider = ExoDatabaseProvider(context)
//    val simpleCache = SimpleCache(context.cacheDir, NoOpCacheEvictor(), exoDatabaseProvider)
//    val defaultDataSourceFactory = DefaultHttpDataSourceFactory()
//    return DownloadManager(
//        context,
//        exoDatabaseProvider,
//        simpleCache,
//        defaultDataSourceFactory,
//        Executors.newFixedThreadPool(5)
//    )
//}
