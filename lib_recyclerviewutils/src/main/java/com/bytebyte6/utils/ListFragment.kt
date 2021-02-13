package com.bytebyte6.utils

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bytebyte6.common.BaseShareFragment
import com.bytebyte6.common.logd
import com.bytebyte6.recyclerviewutils.R

import com.bytebyte6.recyclerviewutils.databinding.FragmentListBinding

abstract class ListFragment : BaseShareFragment<FragmentListBinding>(R.layout.fragment_list) {

    //是否已经加载全部数据
    protected var end = false

    private val listener = object : LoadMoreListener(20) {
        override fun onLoadMore() {
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
            this@ListFragment.onLoadMore()
        }
    }

    private val glideListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                val glideRequest = Glide.with(recyclerView.context)
                if (glideRequest.isPaused) {
                    glideRequest.resumeRequests()
                }
            } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                val glideRequest = Glide.with(recyclerView.context)
                if (!glideRequest.isPaused) {
                    glideRequest.pauseRequests()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startPostponedEnterTransition = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.recyclerView = binding?.recyclerview
        binding?.run {
            recyclerview.addOnScrollListener(listener)
            recyclerview.addOnScrollListener(glideListener)
            recyclerview.doOnPreDraw {
                startPostponedEnterTransition()
            }
            swipeRefreshLayout.setOnRefreshListener {
                end = false
                onRefresh()
            }
        }
    }

    override fun onDestroyView() {
        binding?.recyclerview?.clearOnScrollListeners()
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

    fun hideSwipeRefreshDelay(delay: Long = 1000) {
        binding?.run {
            swipeRefreshLayout.postDelayed({
                swipeRefreshLayout.isRefreshing = false
            }, delay)
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