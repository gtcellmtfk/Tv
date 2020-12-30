package com.bytebyte6.view

import android.os.Bundle
import com.bytebyte6.base.BaseActivity

class VideoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.main_container,
                    VideoFragment().apply {
                        arguments = intent.extras
                    },
                    VideoFragment.TAG
                )
                .commit()
        }
    }
}