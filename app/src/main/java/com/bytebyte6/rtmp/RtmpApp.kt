package com.bytebyte6.rtmp

import android.app.Application
import com.bytebyte6.base.baseModule
import com.bytebyte6.data.dataModule
import com.bytebyte6.logic.logicModule
import com.bytebyte6.view.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RtmpApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@RtmpApp)
            modules(baseModule,dataModule, logicModule, viewModule)
        }
    }
}