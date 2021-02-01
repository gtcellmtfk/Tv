package com.bytebyte6.view

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.bytebyte6.viewmodel.viewModule
import com.bytebyte6.base.LogEx
import com.bytebyte6.base.baseModule
import com.bytebyte6.base.logd
import com.bytebyte6.data.dataModule
import com.bytebyte6.data.roomModule
import com.bytebyte6.usecase.exoPlayerModule
import com.bytebyte6.usecase.work.AppDelegatingWorkerFactory
import com.bytebyte6.usecase.useCaseModule
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RtmpApp : Application() {

    private val factory by inject<AppDelegatingWorkerFactory>()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@RtmpApp)
            modules(baseModule, roomModule, dataModule,
                viewModule, useCaseModule, exoPlayerModule)
        }
        WorkManager.initialize(
            this, Configuration.Builder().setWorkerFactory(
                factory
            ).build()
        )
        RxJavaPlugins.setErrorHandler {
            logd("Rx Global Exception Handler...${it.message}")
            it.printStackTrace()
        }
        LogEx.logger=true
    }
}