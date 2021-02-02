package com.bytebyte6.viewmodel

import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File
import java.util.*
import java.util.concurrent.Executors

val testExoPlayerModule= module {
    /**ExoPlayer*/
    factory <HttpDataSource.Factory> { DefaultHttpDataSourceFactory() }
    factory {
        DownloadManager(
            androidContext(),
            get(DatabaseProvider::class.java),
            get(Cache::class.java),
            get(HttpDataSource.Factory::class.java),
            Executors.newFixedThreadPool(5)
        )
    }
    factory<DatabaseProvider> { ExoDatabaseProvider(androidContext()) }
    factory<Cache> {
        SimpleCache(
            File(androidContext().cacheDir, UUID.randomUUID().toString()) ,
            NoOpCacheEvictor(),
            get(DatabaseProvider::class.java)
        )
    }
}