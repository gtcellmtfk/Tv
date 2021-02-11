package com.bytebyte6.view

import android.app.Application
import android.net.TrafficStats
import android.os.StrictMode
import androidx.work.Configuration
import androidx.work.WorkManager
import com.bytebyte6.viewmodel.viewModule
import com.bytebyte6.common.LogEx
import com.bytebyte6.common.baseModule
import com.bytebyte6.common.logd
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
        LogEx.logger = true
//        strictMode()
    }

    private fun strictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .build()
            )
        }
    }
}