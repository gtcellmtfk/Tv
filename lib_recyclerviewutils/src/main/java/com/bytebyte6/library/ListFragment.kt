package com.bytebyte6.library

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.bytebyte6.base.BaseShareFragment
import com.bytebyte6.library.databinding.FragmentListBinding

abstract class ListFragment : BaseShareFragment<FragmentListBinding>(R.layout.fragment_list) {

    //是否已经加载全部数据
    protected var end = false

    private val listener = object : BottomingListener() {
        override fun onBottom() {
            if (binding == null) {
                return
            }
            if (end) {
                return
            }
            if (binding!!.swipeRefreshLayout.isRefreshing) {
                return
            }
            if (binding!!.linearProgressIndicator.isVisible) {
                return
            }
            onLoadMore()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.run {
            recyclerview.addOnScrollListener(listener)

            swipeRefreshLayout.setOnRefreshListener {
                end = false
                onRefresh()
            }
        }
    }

    override fun onDestroyView() {
        binding?.recyclerview?.removeOnScrollListener(listener)
        binding?.swipeRefreshLayout?.setOnRefreshListener(null)
        super.onDestroyView()
    }

    fun disEnabledSwipeRefreshLayout() {
        binding?.run {
            swipeRefreshLayout.isEnabled = false
        }
    }

    fun showSwipeRefresh() {
        binding?.run {
            swipeRefreshLayout.isRefreshing = true
        }
    }

    fun hideSwipeRefresh() {
        binding?.run {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    fun showProgress() {
        binding?.run {
            if (!swipeRefreshLayout.isRefreshing) {
                linearProgressIndicator.show()
            }
        }
    }

    fun hideProgress() {
        binding?.run {
            linearProgressIndicator.hide()
        }
    }

    override fun initViewBinding(view: View): FragmentListBinding? {
        return FragmentListBinding.bind(view)
    }

    abstract fun onLoadMore()
    abstract fun onRefresh()
}