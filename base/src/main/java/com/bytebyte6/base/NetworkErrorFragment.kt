package com.bytebyte6.base

import android.view.View
import com.bytebyte6.base.databinding.FragmentNetworkErrorBinding

class NetworkErrorFragment :
    BaseFragment<FragmentNetworkErrorBinding>(R.layout.fragment_network_error) {

    companion object{
        const val TAG="NetworkErrorFragment"
    }

    private var navClickListener: ((view: View) -> Unit)? = null

    override fun initBaseViewModelDelegate(): BaseViewModelDelegate? {
        return null
    }

    override fun initBinding(view: View): FragmentNetworkErrorBinding {
        val binding = FragmentNetworkErrorBinding.bind(view)
        binding.toolbar.title = getString(R.string.network_disconnected)
        binding.toolbar.setNavigationOnClickListener {
            navClickListener?.invoke(it)
        }
        return binding
    }

    fun setNavigationOnClickListener(navigationOnClickListener: ((view: View) -> Unit)) {
        navClickListener = navigationOnClickListener
    }
}