package com.bytebyte6.data

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


val dataModule = module {

    single { createRetrofit(get()) }

    single<IpTvApi> { get(Retrofit::class.java).create(IpTvApi::class.java) }

    single { createDb(androidApplication()) }

    single { get(AppDatabase::class.java).ipTvDao() }

    factory<IpTvRepository> { IpTvRepositoryImpl(get(), get(), androidApplication(),get()) }

    factory { Converter() }

    factory {
        GsonBuilder()
            //.excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapterFactory(GsonConfig.NullStringToEmptyAdapterFactory())
            .create()
    }

    factory {
        GsonConverterFactory.create(get())
    }
}

private fun createDb(context: Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, "rtmp.db").build()
}

private fun createRetrofit(gsonConverterFactory: GsonConverterFactory): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://iptv-org.github.io/iptv/")
        .addConverterFactory(gsonConverterFactory)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
}

