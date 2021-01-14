package com.bytebyte6.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.bytebyte6.base.ErrorUtils
import com.bytebyte6.base.mvi.branchIfNotHandled
import com.bytebyte6.base_ui.BaseActivity
import com.bytebyte6.base_ui.Message
import com.bytebyte6.base_ui.showSnack
import com.bytebyte6.view.databinding.ActivityLauncherBinding
import org.koin.android.viewmodel.ext.android.viewModel

class LauncherActivity : BaseActivity() {

    private val viewModel by viewModel<LauncherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.init().observe(this, Observer {
            it.branchIfNotHandled(
                success = {
                    binding.root.postDelayed({
                        val intent = Intent(
                            this@LauncherActivity,
                            MainActivity::class.java
                        )
                        startActivity(intent)
                        finish()
                    }, 1500)
                },
                error = {
                    showSnack(
                        binding.root,
                        Message(
                            id = ErrorUtils.getMessage(it.error)
                        )
                    )
                }
            )
        })
    }
}