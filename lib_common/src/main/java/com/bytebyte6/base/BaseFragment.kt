package com.bytebyte6.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


abstract class BaseFragment<V : ViewBinding>(layoutId: Int) : Fragment(layoutId) {

    var binding: V? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        logd("onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logd("onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logd("onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logd("onViewCreated")
        binding = initViewBinding(view)
    }

    override fun onStart() {
        super.onStart()
        logd("onStart")
    }

    override fun onResume() {
        super.onResume()
        logd("onResume")
    }

    override fun onStop() {
        super.onStop()
        logd("onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logd("onDestroyView view=${view}")
        binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        logd("onDestroy view=${view}")
    }

    override fun onDetach() {
        super.onDetach()
        logd("onDetach")
    }

    abstract fun initViewBinding(view: View): V?
}

