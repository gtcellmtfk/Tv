package com.bytebyte6.base

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Transition
import androidx.viewbinding.ViewBinding
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform

const val KEY_TRANS_NAME = "KEY_TRANS_NAME"

abstract class BaseShareFragment<V : ViewBinding>(layoutId: Int) : BaseFragment<V>(layoutId),
    RecyclerViewClearHelper {

    protected var startPostponedEnterTransition = true

    override var recyclerView: RecyclerView? = null

    override var imageClearHelper: ImageClearHelper? = null

    private val exitTransitionListener = object : DefaultTransitionListener() {
        override fun onTransitionEnd(transition: Transition) {
            this@BaseShareFragment.logd("exitTransitionListener onTransitionEnd")
        }
    }
    private val sharedTransitionListener = object : DefaultTransitionListener() {
        override fun onTransitionEnd(transition: Transition) {
            this@BaseShareFragment.logd("sharedTransitionListener onTransitionEnd")
        }
    }

    init {
        exitTransition = Hold().apply {
            addListener(exitTransitionListener)
        }
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            addListener(sharedTransitionListener)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString(KEY_TRANS_NAME)
        view.transitionName = name
        postponeEnterTransition()
        if (startPostponedEnterTransition) {
            view.doOnPreDraw {
                startPostponedEnterTransition()
            }
        }
    }

//    abstract fun onExitTransEnd()
//    abstract fun onShareReturnTransEnd()
}

