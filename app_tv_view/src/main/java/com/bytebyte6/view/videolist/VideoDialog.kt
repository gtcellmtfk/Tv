package com.bytebyte6.view.videolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bytebyte6.base_ui.KEY_TRANS_NAME
import com.bytebyte6.view.ImageAdapter
import com.bytebyte6.view.KEY_ITEM

import com.bytebyte6.view.databinding.DialogVideoBinding
import com.bytebyte6.view.showVideoActivity
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

        val adapter = ImageAdapter()
        adapter.setOnItemClick { pos, _ ->
            dismiss()
            showVideoActivity(adapter.currentList[pos].videoUrl)
        }

        binding?.apply {
            recyclerView.adapter = adapter
        }
//
//        viewModel.search(requireArguments().getString(KEY_ITEM)!!).observe(this, Observer {
//            adapter.submitList(it)
//            binding?.tvTotal?.apply {
//                text = getString(R.string.total, it.size)
//            }
//        })
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}