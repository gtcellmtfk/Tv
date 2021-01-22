package com.bytebyte6.base_ui

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.viewbinding.ViewBinding
import com.bytebyte6.base.logd
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform

const val KEY_TRANS_NAME = "KEY_TRANS_NAME"

abstract class BaseShareFragment<V:ViewBinding>(layoutId: Int) : BaseFragment<V>(layoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Hold()
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString(KEY_TRANS_NAME)
        view.transitionName = name
        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }
}

