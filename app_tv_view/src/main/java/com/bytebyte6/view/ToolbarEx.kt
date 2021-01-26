package com.bytebyte6.view

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment


fun Fragment.setupToolbarArrowBack(
    title: String? = null,
    subTitle: String? = null,
    doSomeWorkBeforePop: (() -> Unit)? = null
) {
    val toolbar = requireView().findViewById<Toolbar>(R.id.toolbar)
    if (title != null) {
        toolbar.title = title
    }
    if (subTitle != null) {
        toolbar.subtitle = subTitle
    }
    toolbar.setNavigationOnClickListener {
        doSomeWorkBeforePop?.invoke()
        popBackStack()
    }
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
    val mainActivity = requireActivity() as MainActivity
    mainActivity.lockDrawer()
}

fun Fragment.setupToolbarMenuMode(title: String? = null, subTitle: String? = null) {
    val toolbar = requireView().findViewById<Toolbar>(R.id.toolbar)
    val mainActivity = requireActivity() as MainActivity
    toolbar.let {
        it.setNavigationOnClickListener {
            mainActivity.openDrawer()
        }
        it.setNavigationIcon(R.drawable.ic_menu)
        title?.run {
            it.title = this
        }
        subTitle?.run {
            it.subtitle = this
        }
    }
    mainActivity.unlockDrawer()
}