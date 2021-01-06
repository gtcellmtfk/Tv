package com.bytebyte6.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.bytebyte6.base.BaseActivity
import com.bytebyte6.base.ErrorUtils
import com.bytebyte6.base.Message
import com.bytebyte6.base.mvi.isError
import com.bytebyte6.base.mvi.success
import com.bytebyte6.base.showSnack
import com.bytebyte6.view.databinding.ActivityLauncherBinding
import com.google.android.material.transition.platform.MaterialFadeThrough
import org.koin.android.viewmodel.ext.android.viewModel

class LauncherActivity : BaseActivity() {

    private val viewModel by viewModel<LauncherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val exit = MaterialFadeThrough()
        window.exitTransition = exit
        window.allowEnterTransitionOverlap=true

        val binding = ActivityLauncherBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel.init().observe(this, Observer {
            it.success()?.apply {
                binding.lav.cancelAnimation()
                val bundle =
                    ActivityOptions.makeSceneTransitionAnimation(this@LauncherActivity).toBundle()
                startActivity(Intent(this@LauncherActivity, MainActivity::class.java), bundle)
                finish()
            }
            it.isError()?.apply {
                showSnack(binding.root, Message(id = ErrorUtils.getMessage(this)))
            }
        })
    }
}