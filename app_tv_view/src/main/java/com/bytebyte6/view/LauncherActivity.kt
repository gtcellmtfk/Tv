package com.bytebyte6.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.bytebyte6.base.ErrorUtils
import com.bytebyte6.base.EventObserver
import com.bytebyte6.base.mvi.Result
import com.bytebyte6.base.mvi.doSomethingIfNotHandled
import com.bytebyte6.base_ui.BaseActivity
import com.bytebyte6.base_ui.Message
import com.bytebyte6.base_ui.showSnack
import com.bytebyte6.view.databinding.ActivityLauncherBinding
import com.google.android.material.transition.platform.MaterialFadeThrough
import org.koin.android.viewmodel.ext.android.viewModel

class LauncherActivity : BaseActivity() {

    private val viewModel by viewModel<LauncherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val exit = MaterialFadeThrough()
        window.exitTransition = exit
        window.allowEnterTransitionOverlap = true

        val binding = ActivityLauncherBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel.init().observe(this, Observer {
            when (it) {
                is Result.Success -> {
                    it.doSomethingIfNotHandled {
                        binding.lav.cancelAnimation()
                        val bundle = ActivityOptions.makeSceneTransitionAnimation(this@LauncherActivity).toBundle()
                        val intent = Intent(this@LauncherActivity, MainActivity::class.java)
                        startActivity(intent, bundle)
                        finish()
                    }
                }
                is Result.Error -> {
                    it.doSomethingIfNotHandled {
                        showSnack(
                            binding.root,
                            Message(
                                id = ErrorUtils.getMessage(it.error)
                            )
                        )
                    }
                }
                else -> {
                }
            }
        })
    }
}