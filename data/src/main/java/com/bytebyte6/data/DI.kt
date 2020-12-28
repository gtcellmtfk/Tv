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
    val retrofit = createRetrofit()
    single<IpTvApi> { retrofit.create(IpTvApi::class.java) }

    single<AppDatabase> {
        createDb(androidApplication())
    }

    single<IpTvDao> { get(AppDatabase::class.java).ipTvDao() }

    factory<IpTvRepository> { IpTvRepositoryImpl(get(), get(), get()) }

    factory<Converter> { Converter() }
}

private fun createDb(context: Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, "rtmp").build()
}

private fun createRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://iptv-org.github.io/iptv/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().registerTypeAdapterFactory(
                    GsonConfig.NullStringToEmptyAdapterFactory()
                ).create()
            )
        )
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
}

