package com.bytebyte6.common

import androidx.fragment.app.Fragment
import androidx.transition.Transition

/**
 * 当片段退出过渡动画执行完毕时清理图片资源防止内存泄漏
 * 使用Glide.with(fragment)时，onDestroyView会清除ImageView资源，但是会影响动画观感，显示一片占位图
 * 所以使用该方法以便在动画结束时清理ImageView资源
 *
 * 假设有三个片段A，B，C，三个片段都有图片列表，跳转顺序是A-B-C，
 *
 * A->B 要清除A的ImageView资源
 * 那么在A的在onViewCreate中设置
 * @see doOnExitTransitionEndOneShot(doOnExitTransition)
 *
 * B->A 要清除B的ImageView资源
 * 那么在B的在onCreate中设置
 * @see doOnSharedElementReturnTransitionEnd(doOnReturnTransitionEnd)
 * 因为 popBackStack()执行的退出动画是
 * @see Fragment.getSharedElementReturnTransition
 *
 * B->C 要清除B的ImageView资源
 * 那么在B的在onViewCreate中设置
 * @see doOnExitTransitionEndOneShot(doOnExitTransition)
 *
 * C->B 要清除C的资源
 * 在B的在onCreate中设置
 * @see doOnSharedElementReturnTransitionEnd(doOnReturnTransitionEnd)
 * 因为 popBackStack()执行的退出动画是
 * @see Fragment.getSharedElementReturnTransition
 */
fun Fragment.doOnSharedElementReturnTransitionEnd(
    doOnReturnTransitionEnd: (() -> Unit)? = null
) {
    (sharedElementReturnTransition as Transition?)?.addListener(object :
        DefaultTransitionListener() {
        override fun onTransitionEnd(transition: Transition) {
            if (view == null) {
                doOnReturnTransitionEnd?.invoke()
                transition.removeListener(this)
            }
        }
    })
}

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