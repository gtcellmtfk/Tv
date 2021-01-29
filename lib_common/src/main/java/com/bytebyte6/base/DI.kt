package com.bytebyte6.base

import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val baseModule = module {
    single { NetworkHelper(androidContext()) }
}