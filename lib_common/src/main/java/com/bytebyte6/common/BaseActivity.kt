package com.bytebyte6.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logd("onCreate")
    }

    override fun onStart() {
        super.onStart()
        logd("onStart")
    }

    override fun onResume() {
        super.onResume()
        logd("onResume")
    }

    override fun onPause() {
        super.onPause()
        logd("onPause")
    }

    override fun onStop() {
        super.onStop()
        logd("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        logd("onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        logd("onRestart")
    }
}

