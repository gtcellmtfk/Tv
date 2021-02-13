package com.bytebyte6.common

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform

const val KEY_TRANS_NAME = "KEY_TRANS_NAME"

abstract class BaseShareFragment<V : ViewBinding>(layoutId: Int) : BaseFragment<V>(layoutId),
    RecyclerViewClearHelper {

    /**
     * true 子类接管 startPostponedEnterTransition()
     * false view接管
     */
    protected var startPostponedEnterTransition = true

    /**
     * 用于过渡动画结束时清理资源
     */
    override var recyclerView: RecyclerView? = null

    /**
     * 用于过渡动画结束时清理资源
     */
    override var imageClearHelper: ImageClearHelper? = null

    init {
        /**
         * 退场时保持当前视图知道下个片段进场完成
         */
        exitTransition = Hold()
        /**
         * 默认动画
         */
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString(KEY_TRANS_NAME)
        view.transitionName = name

        // 延迟片段转换直到startPostponedEnterTransition()被调用
        postponeEnterTransition()
        if (startPostponedEnterTransition) {
            view.doOnPreDraw {
                startPostponedEnterTransition()
            }
        }
    }
}

