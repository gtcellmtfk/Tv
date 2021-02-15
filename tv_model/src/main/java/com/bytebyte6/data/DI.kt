package com.bytebyte6.data

import android.content.Context
import androidx.room.Room
import com.bytebyte6.common.GsonConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 实际环境使用
 */
val roomModule = module {
    single { createDb(androidContext()) }
}

val dataModule = module {
    single<DataManager> { DataManagerImpl(get(AppDatabase::class)) }
    factory {
        val nullStringToEmptyAdapterFactory = GsonConfig.NullStringToEmptyAdapterFactory()
        GsonBuilder().registerTypeAdapterFactory(nullStringToEmptyAdapterFactory).create()
    }
    factory { GsonConverterFactory.create(get()) }
}

private fun createDb(context: Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, "rtmp.db").build()
}

