package com.bytebyte6.view.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ShareCompat
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.bytebyte6.viewmodel.HomeViewModel
import com.bytebyte6.common.BaseShareFragment
import com.bytebyte6.common.doOnExitTransitionEndOneShot
import com.bytebyte6.view.*
import com.bytebyte6.view.adapter.TabAdapter
import com.bytebyte6.view.databinding.FragmentHomeBinding
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

    private var tabAdapter: TabAdapter? = null

    private fun destroyViewPage() {
        tabAdapter?.fs?.forEach {
            //清理子片段资源
            it.clearRecyclerView()
        }
        tabAdapter?.fs?.clear()
        tabAdapter = null
        //清除ViewPage资源
        mediator?.detach()
        mediator = null
        viewPager2?.adapter = null
        viewPager2 = null
    }

    private fun showDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.tip))
            .setMessage(getString(R.string.enter_exit))
            .setPositiveButton(R.string.enter) { dialog, _ ->
                dialog.dismiss()
                requireActivity().finish()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    override fun initViewBinding(view: View): FragmentHomeBinding {
        return FragmentHomeBinding.bind(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarMenuMode()
        doOnExitTransitionEndOneShot {
            destroyViewPage()
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this.viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showDialog(view.context)
                }
            }
        )
        binding?.apply {
            toolbar.transitionName = getString(R.string.search_share)
            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.app_bar_share) {
                    share()
                } else {
                    homeToSearch(toolbar)
                }
                true
            }
            viewPager2 = viewPager
            viewPager.isUserInputEnabled = false
            tabAdapter = TabAdapter(this@HomeFragment)
            viewPager.adapter = tabAdapter
            mediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    TAB_COUNTRY -> tab.apply {
                        setIcon(R.drawable.ic_flag_2)
                        setText(R.string.home_country)
                    }
                    TAB_LANGUAGE -> tab.apply {
                        setIcon(R.drawable.ic_language)
                        setText(R.string.home_language)
                    }
                    TAB_CATEGORY -> tab.apply {
                        setIcon(R.drawable.ic_category)
                        setText(R.string.home_category)
                    }
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
        //解决fitSystemWindow失效的问题
        view.doOnPreDraw {
            it.requestLayout()
        }
    }

    private fun share() {
        ShareCompat.IntentBuilder
            .from(requireActivity())
            .setText("https://github.com/bytebyte6")
            .setType("text/plain")
            .startChooser()
    }
}