package com.bytebyte6.view.launcher

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import com.bytebyte6.viewmodel.LauncherViewModel
import com.bytebyte6.common.emitIfNotHandled
import com.bytebyte6.common.BaseActivity
import com.bytebyte6.common.Message
import com.bytebyte6.common.showToast
import com.bytebyte6.view.main.MainActivity
import org.koin.android.viewmodel.ext.android.viewModel

class LauncherActivity : BaseActivity() {

    private val viewModel by viewModel<LauncherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.start().observe(this, Observer { result ->
            result.emitIfNotHandled(
                success = {
                    viewModel.obs()
                    Handler().postDelayed({
                        val intent = Intent(
                            this@LauncherActivity,
                            MainActivity::class.java
                        )
                        startActivity(intent)
                        finish()
                    }, 1000)
                },
                error = {
                    showToast(
                        Message(
                            message = it.error.message.toString()
                        )
                    )
                }
            )
        })
    }
}