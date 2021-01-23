package com.bytebyte6.view.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ShareCompat
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.transition.Transition
import androidx.viewpager2.widget.ViewPager2
import com.bytebyte6.base.logd
import com.bytebyte6.base_ui.BaseShareFragment
import com.bytebyte6.view.*
import com.bytebyte6.view.databinding.FragmentHomeBinding
import com.bytebyte6.view.search.SearchFragment
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.viewmodel.ext.android.viewModel

/***
 * 首页
 */
class HomeFragment : BaseShareFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    companion object {
        const val TAG = "HomeFragment"
    }

    private val viewModel: HomeViewModel by viewModel()

    private var viewPager2: ViewPager2? = null

    private var mediator: TabLayoutMediator? = null

    fun destroyViewPage() {
        logd("viewPage2 $viewPager2")
        mediator?.detach()
        mediator = null
        viewPager2?.adapter = null
        viewPager2 = null
        logd("viewPage2 $viewPager2")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupOnBackPressedDispatcherBackToHome()
    }

    override fun initViewBinding(view: View): FragmentHomeBinding {
        return FragmentHomeBinding.bind(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarMenuMode()
        binding?.apply {
            toolbar.transitionName = getString(R.string.search_share)
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
            viewPager2 = viewPager
            viewPager.isUserInputEnabled = false
            viewPager.adapter = TabAdapter(this@HomeFragment)
            mediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    TAB_COUNTRY -> tab.setText(R.string.home_country)
                    TAB_LANGUAGE -> tab.setText(R.string.home_language)
                    TAB_CATEGORY -> tab.setText(R.string.home_category)
                }
            }
            mediator?.attach()
        }
        viewModel.apply {
            category.observe(viewLifecycleOwner, Observer {
                binding?.tabLayout?.getTabAt(TAB_CATEGORY)?.orCreateBadge?.number = it.size
            })

            cs.observe(viewLifecycleOwner, Observer {
                binding?.tabLayout?.getTabAt(TAB_COUNTRY)?.orCreateBadge?.number = it.size
            })

            lang.observe(viewLifecycleOwner, Observer {
                binding?.tabLayout?.getTabAt(TAB_LANGUAGE)?.orCreateBadge?.number = it.size
            })
        }
        //解决状态栏失效不见的问题
        view.doOnPreDraw {
            it.requestLayout()
        }
    }
}