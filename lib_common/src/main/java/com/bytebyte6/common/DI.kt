package com.bytebyte6.common

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val baseModule = module {
    single { NetworkHelper(androidContext()) }
}