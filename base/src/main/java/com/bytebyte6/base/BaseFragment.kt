package com.bytebyte6.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<Binding : ViewBinding>(layoutId: Int) : Fragment(layoutId) {

    private val baseViewModel : BaseViewModel? by lazy { initViewModel() }

    protected var binding: Binding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = initBinding(view)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    protected fun initToast() {
        baseViewModel?.getToastLiveData()?.observe(this, EventObserver {
            showToast(it)
        })
    }

    protected fun showToast(it: Message) {
        Toast.makeText(
            context,
            it.get(context!!),
            if (it.longDuration) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        ).show()
    }

    protected fun initSnack(view: View, listener: View.OnClickListener? = null) {
        baseViewModel?.getSnackBarLiveData()?.observe(this, EventObserver {
            val bar = Snackbar.make(
                view,
                it.get(context!!),
                if (it.longDuration) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
            )
            if (it.actionId != 0) {
                bar.setAction(it.actionId, listener)
            }
            bar.show()
        })
    }

    protected fun initShowLoading(loadingView: View) {
        baseViewModel?.getLoadingLiveData()?.observe(this, EventObserver { showLoading ->
            loadingView.visibility = if (showLoading) View.VISIBLE else View.GONE
        })
    }

    abstract fun initViewModel(): BaseViewModel?
    abstract fun initBinding(view: View): Binding
}

