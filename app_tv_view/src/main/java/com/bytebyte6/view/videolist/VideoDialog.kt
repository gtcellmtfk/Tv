package com.bytebyte6.view.videolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bytebyte6.base.ResultObserver
import com.bytebyte6.base.KEY_TRANS_NAME
import com.bytebyte6.data.entity.TvFts
import com.bytebyte6.view.*

import com.bytebyte6.view.databinding.DialogVideoBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.viewmodel.ext.android.viewModel

class VideoDialog : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "VideoDialog"
        fun newInstance(transName: String, item: String): VideoDialog {
            return VideoDialog().apply {
                arguments = Bundle().apply {
                    putString(KEY_TRANS_NAME, transName)
                    putString(KEY_ITEM, item)
                }
            }
        }
    }

    private var binding: DialogVideoBinding? = null

    private val viewModel by viewModel<VideoListViewModel>()

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
        val title = requireArguments().getString(KEY_TITLE)!!
        val adapter = ImageAdapter()
        adapter.onItemClick= { pos , _: View->
            dismiss()
            toPlayer(adapter.currentList[pos].videoUrl)
        }

        binding?.apply {
            recyclerView.adapter = adapter
            recyclerView.itemAnimator = null
        }

        viewModel.count(title).observe(viewLifecycleOwner, Observer {
            binding?.tvTotal?.apply {
                text = getString(R.string.total, it)
            }
        })

        viewModel.tvs.observe(viewLifecycleOwner, object : ResultObserver<List<TvFts>>() {
            override fun success(data: List<TvFts>, end: Boolean) {
                adapter.submitList(TvFts.toTvs(data))
            }
        })
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}