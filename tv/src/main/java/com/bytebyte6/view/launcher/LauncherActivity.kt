package com.bytebyte6.view.launcher

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import com.bytebyte6.common.BaseActivity
import com.bytebyte6.common.Message
import com.bytebyte6.common.emitIfNotHandled
import com.bytebyte6.common.showToast
import com.bytebyte6.view.main.MainActivity
import com.bytebyte6.viewmodel.LauncherViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class LauncherActivity : BaseActivity() {

    companion object {
        var mainActivityDestroy = true
    }

    private val viewModel by viewModel<LauncherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (mainActivityDestroy) {
            start()
        } else {
            toMain()
        }
    }

    private fun start() {
        viewModel.start().observe(this, Observer { result ->
            result.emitIfNotHandled(
                success = { success ->
                    success.data.let {
                        AppCompatDelegate.setDefaultNightMode(
                            if (it.nightMode) AppCompatDelegate.MODE_NIGHT_YES
                            else AppCompatDelegate.MODE_NIGHT_NO
                        )
                    }
                    Handler().postDelayed({ toMain() }, 400)
                }, error = {
                    showToast(
                        Message(
                            message = it.error.message.toString()
                        )
                    )
                    finish()
                }, {
                    // loading
                }
            )
        })
    }

    private fun toMain() {
        val intent = Intent(this@LauncherActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}