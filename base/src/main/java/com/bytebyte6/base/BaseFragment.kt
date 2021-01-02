package com.bytebyte6.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<Binding : ViewBinding>(layoutId: Int) : Fragment(layoutId) {

    protected var baseViewModelDelegate: BaseViewModelDelegate? = null

    protected var binding: Binding? = null

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        logd("onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logd("onViewCreated")
        binding = initBinding(view)
        baseViewModelDelegate = initBaseViewModelDelegate()
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
        binding = null
        super.onDestroyView()
        logd("onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        logd("onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        logd("onDetach")
    }

    protected fun observeToast() {
        baseViewModelDelegate?.toast?.observe(this, EventObserver {
            showToast(it)
        })
    }

    protected fun observeSnack(view: View, listener: View.OnClickListener? = null) {
        baseViewModelDelegate?.snackBar?.observe(this, EventObserver {
            showSnack(view, it, listener)
        })
    }

    protected fun observeLoading(loadingView: View) {
        baseViewModelDelegate?.loading?.observe(this, EventObserver { showLoading ->
            loadingView.visibility = if (showLoading) View.VISIBLE else View.GONE
        })
    }

    abstract fun initBaseViewModelDelegate(): BaseViewModelDelegate?
    abstract fun initBinding(view: View): Binding?
}

