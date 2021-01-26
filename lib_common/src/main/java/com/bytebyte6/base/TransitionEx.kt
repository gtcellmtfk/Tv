package com.bytebyte6.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.transition.Transition

/**
 * 当片段退出过渡动画执行完毕时清理图片资源防止内存泄漏
 * 使用Glide.with(fragment)时，onDestroyView会清除ImageView资源，但是会影响动画观感，显示一片占位图
 * 所以使用该方法以便在动画结束时清理ImageView资源
 * 在onCreate中设置
 */
fun Fragment.doOnSharedElementReturnTransitionEnd(
    doOnExitTransition: (() -> Unit)? = null
) {
    (sharedElementReturnTransition as Transition?)?.addListener(object :
        DefaultTransitionListener() {
        override fun onTransitionEnd(transition: Transition) {
            if (view == null) {
                doOnExitTransition?.invoke()
                transition.removeListener(this)
            }
        }
    })
}

/**
 * 在onViewCreate中设置
 */
fun Fragment.doOnExitTransitionEndOneShot(doOnExitTransition: () -> Unit) {
    val trans = exitTransition as Transition?
    val defaultTransitionListener = object : DefaultTransitionListener() {
        override fun onTransitionEnd(transition: Transition) {
            if (view == null) {
                doOnExitTransition.invoke()
                trans?.removeListener(this)
            }
        }
    }
    trans?.addListener(defaultTransitionListener)
}