package com.bytebyte6.logic

import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val logicModule: Module = module {
    viewModel { IpTvViewModel(get(), get(),androidApplication()) }
}