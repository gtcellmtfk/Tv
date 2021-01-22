package com.bytebyte6.view.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ShareCompat
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import com.bytebyte6.base.mvi.emitIfNotHandled
import com.bytebyte6.base_ui.BaseFragment
import com.bytebyte6.base_ui.BaseShareFragment
import com.bytebyte6.base_ui.Message
import com.bytebyte6.base_ui.showSnack
import com.bytebyte6.view.*
import com.bytebyte6.view.databinding.FragmentHomeBinding
import com.bytebyte6.view.search.SearchFragment
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.viewmodel.ext.android.viewModel

/***
 * 首页
 */
class HomeFragment : BaseShareFragment(R.layout.fragment_home) {

    companion object {
        const val TAG = "HomeFragment"
    }

    private val viewModel: HomeViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    AlertDialog.Builder(context)
                        .setTitle(getString(R.string.tip))
                        .setMessage(getString(R.string.enter_exit))
                        .setPositiveButton(R.string.enter) { dialog, which ->
                            dialog.dismiss()
                            requireActivity().finish()
                        }
                        .setNegativeButton(R.string.cancel) { dialog, which -> dialog.dismiss() }
                        .create()
                        .show()
                }
            }
        )
    }

    override fun onViewCreated(view: View): FragmentHomeBinding {
        return FragmentHomeBinding.bind(view).apply {

            toolbar.transitionName = getString(R.string.search_share)

            setupToolbarMenuMode()

            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.app_bar_share) {
                    ShareCompat.IntentBuilder
                        .from(requireActivity())
                        .setText("https://github.com/bytebyte6")
                        .setType("text/plain")
                        .startChooser()
                } else {
                    replaceWithShareElement(
                        SearchFragment.newInstance(toolbar.transitionName),
                        SearchFragment.TAG,
                        toolbar
                    )
                }
                true
            }

            viewPager.adapter = TabAdapter(this@HomeFragment)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    TAB_COUNTRY -> tab.setText(R.string.home_country)
                    TAB_LANGUAGE -> tab.setText(R.string.home_language)
                    TAB_CATEGORY -> tab.setText(R.string.home_category)
                }
            }.attach()

            viewModel.apply {
                category.observe(viewLifecycleOwner, Observer {
                    tabLayout.getTabAt(TAB_CATEGORY)?.orCreateBadge?.number = it.size
                })

                cs.observe(viewLifecycleOwner, Observer {
                    tabLayout.getTabAt(TAB_COUNTRY)?.orCreateBadge?.number = it.size
                })

                lang.observe(viewLifecycleOwner, Observer {
                    tabLayout.getTabAt(TAB_LANGUAGE)?.orCreateBadge?.number = it.size
                })
            }

            //解决状态栏失效不见的问题
            view.doOnPreDraw {
                it.requestLayout()
            }
        }
    }
}