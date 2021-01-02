package com.bytebyte6.view.video

import androidx.lifecycle.ViewModel
import com.bytebyte6.base.BaseViewModelDelegate

class VideoViewModel(
    private val baseViewModelDelegate: BaseViewModelDelegate
) : ViewModel(), BaseViewModelDelegate by baseViewModelDelegate {


}