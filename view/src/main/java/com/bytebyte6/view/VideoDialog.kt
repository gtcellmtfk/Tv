package com.bytebyte6.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bytebyte6.data.model.IpTv
import com.bytebyte6.logic.IpTvViewModel
import com.bytebyte6.view.databinding.DialogVideoBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.viewmodel.ext.android.sharedViewModel

class VideoDialog(private val tab: Int, private val clickItem: Any) : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "VideoDialog"
    }

    private var binding: DialogVideoBinding? = null

    private val viewModel by sharedViewModel<IpTvViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogVideoBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = VideoAdapter()
        adapter.setOnItemClick {
            dismiss()
            startActivity(Intent(context, VideoActivity::class.java).apply {
                putExtra(IpTv.TAG, adapter.currentList[it])
            })
        }

        binding?.apply {
            recyclerView.adapter = adapter
        }

        viewModel.ipTvsLiveData(tab, clickItem)?.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}