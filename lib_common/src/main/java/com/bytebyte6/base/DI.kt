package com.bytebyte6.base

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val baseModule = module {
    single { NetworkHelper(androidApplication()) }
}