package com.bytebyte6.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import com.bytebyte6.base.emitIfNotHandled
import com.bytebyte6.base.BaseActivity
import com.bytebyte6.base.Message
import com.bytebyte6.base.showToast
import org.koin.android.viewmodel.ext.android.viewModel

class LauncherActivity : BaseActivity() {

    private val viewModel by viewModel<LauncherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.start().observe(this, Observer { result ->
            result.emitIfNotHandled(
                success = {
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