package com.bytebyte6.view.video

import android.os.Bundle
import com.bytebyte6.base.BaseActivity
import com.bytebyte6.view.R
import com.bytebyte6.view.replaceNotAddToBackStack

class VideoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            replaceNotAddToBackStack(
                VideoFragment().apply { arguments = intent.extras },
                VideoFragment.TAG
            )
        }
    }
}