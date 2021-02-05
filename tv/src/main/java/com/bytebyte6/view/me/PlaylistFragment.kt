package com.bytebyte6.view.me

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bytebyte6.viewmodel.PlaylistViewModel
import com.bytebyte6.common.*
import com.bytebyte6.utils.GridSpaceDecoration
import com.bytebyte6.utils.ListFragment
import com.bytebyte6.usecase.UpdateTvParam
import com.bytebyte6.view.*
import com.bytebyte6.view.R
import com.bytebyte6.view.adapter.ButtonClickListener
import com.bytebyte6.view.adapter.ButtonType
import com.bytebyte6.view.adapter.ImageAdapter
import com.bytebyte6.view.download.DownloadServicePro
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.offline.DownloadHelper
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.HttpDataSource
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException

/***
 * 播放列表
 */
class PlaylistFragment : ListFragment(), DownloadManager.Listener {

    companion object {
        fun newInstance(
            playlistId: Long,
            title: String,
            transitionName: String
        ): Fragment {
            return PlaylistFragment().apply {
                arguments = Bundle().apply {
                    putLong(KEY_PLAY_LIST_ID, playlistId)
                    putString(KEY_TITLE, title)
                    putString(KEY_TRANS_NAME, transitionName)
                }
            }
        }

        const val TAG = "PlaylistFragment"
    }

    private val httpDataSourceFactory by inject<HttpDataSource.Factory>()

    private val defaultRenderersFactory by lazy { DefaultRenderersFactory(requireContext()) }

    private var downloadHelper: DownloadHelper? = null

    private val viewModel: PlaylistViewModel by viewModel()

    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doOnSharedElementReturnTransitionEnd {
            clearRecyclerView()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarArrowBack()
        disEnabledSwipeRefreshLayout()
        showSwipeRefresh()
        val imageAdapter = ImageAdapter(
            ButtonType.DOWNLOAD,
            object : ButtonClickListener {
                override fun onClick(position: Int) {
                    onDownloadClick(position)
                }
            }).apply {
            onItemClick = { pos, _: View ->
                toPlayer(currentList[pos].videoUrl)
            }
            doOnBind = { pos, _: View ->
                if (recyclerView!!.scrollState== RecyclerView.SCROLL_STATE_IDLE){
                viewModel.searchLogo(pos)
                }
            }
            onCurrentListChanged = { _, c ->
                binding?.emptyBox?.isVisible = c.isEmpty()
            }
        }
        imageClearHelper = imageAdapter

        binding?.apply {
            appbar.toolbar.title = requireArguments().getString(KEY_TITLE)
            recyclerview.adapter = imageAdapter
            recyclerview.addItemDecoration(GridSpaceDecoration())
            recyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerview.setHasFixedSize(true)
            recyclerview.itemAnimator = null
        }
        viewModel.apply {
            tvs(requireArguments().getLong(KEY_PLAY_LIST_ID))
                .observe(viewLifecycleOwner, Observer {
                    hideSwipeRefresh()
                    imageAdapter.submitList(it)
                    binding?.appbar?.toolbar?.subtitle =
                        getString(R.string.total, imageAdapter.itemCount)
                })
            updateTv.observe(viewLifecycleOwner, object : ResultObserver<UpdateTvParam>() {
                override fun successOnce(data: UpdateTvParam, end: Boolean) {
                    imageAdapter.notifyItemChanged(data.pos)
                }

                override fun error(error: Throwable) {
                    showSnack(requireView(), Message(message = error.message.toString()))
                }
            })
        }
    }

    private fun onDownloadClick(pos: Int) {
        viewModel.apply {
            showProgressDialog()
            downloadHelper?.release()
            downloadHelper = null
            downloadHelper = DownloadHelper.forMediaItem(
                requireContext(),
                MediaItem.fromUri(getTv(pos).url),
                defaultRenderersFactory,
                httpDataSourceFactory
            )
            downloadHelper!!.prepare(object : DownloadHelper.Callback {
                override fun onPrepared(helper: DownloadHelper) {
                    download(pos)
                    DownloadServicePro.addDownload(requireContext(), getTv(pos).url)
                    val tip = getString(R.string.tip_add_download_has_been)
                    showSnack(requireView(), Message(message = tip))
                    hideProgressDialog()
                }

                override fun onPrepareError(helper: DownloadHelper, e: IOException) {
                    if (e is DownloadHelper.LiveContentUnsupportedException) {
                        showSnack(
                            requireView(),
                            Message(id = R.string.tip_un_support_download_live_stream)
                        )
                    } else {
                        showSnack(
                            requireView(),
                            Message(message = e.message.toString())
                        )
                    }
                    hideProgressDialog()
                }
            })
        }
    }

    private fun showProgressDialog() {
        progressDialog = ProgressDialog(requireContext()).apply {
            setTitle(R.string.tip)
            setMessage(getString(R.string.tip_please_wait))
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
                this@PlaylistFragment.progressDialog = null
                downloadHelper?.release()
                downloadHelper = null
            }
        }
        progressDialog!!.show()
    }

    private fun hideProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    override fun onDestroyView() {
        downloadHelper?.release()
        downloadHelper = null
        super.onDestroyView()
    }

    override fun onLoadMore() {

    }

    override fun onRefresh() {

    }
}
