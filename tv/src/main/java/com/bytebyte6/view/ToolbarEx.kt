package com.bytebyte6.view

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bytebyte6.view.main.MainActivity

fun Fragment.setupToolbarArrowBack(
    title: String? = null,
    subTitle: String? = null,
    doSomeWorkBeforePop: (() -> Unit)? = null
) {
    val toolbar = requireView().findViewById<Toolbar>(R.id.toolbar)
    toolbar.contentDescription = getString(R.string.toolbarContentDescription)
    toolbar.navigationContentDescription = getString(R.string.toolbar_navigation)
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
    val drawerHelper = (requireActivity() as MainActivity).drawerHelper
    drawerHelper.lockDrawer()
}

fun Fragment.setupToolbarMenuMode(
    title: String? = null,
    subTitle: String? = null,
    doSomeInNavigation: (() -> Unit)? = null
) {
    val toolbar = requireView().findViewById<Toolbar>(R.id.toolbar)
    val drawerHelper = (requireActivity() as MainActivity).drawerHelper
    toolbar.let {
        it.contentDescription = getString(R.string.toolbarContentDescription)
        it.navigationContentDescription = getString(R.string.toolbar_navigation)
        it.setNavigationOnClickListener {
            doSomeInNavigation?.invoke()
            drawerHelper.openDrawer()
        }
        it.setNavigationIcon(R.drawable.ic_menu)
        title?.run {
            it.title = this
        }
        subTitle?.run {
            it.subtitle = this
        }
    }
    drawerHelper.unlockDrawer()
}