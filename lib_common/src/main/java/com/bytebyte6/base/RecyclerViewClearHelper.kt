package com.bytebyte6.base

import androidx.recyclerview.widget.RecyclerView

interface RecyclerViewClearHelper {
    var recyclerView: RecyclerView?
    var imageClearHelper: ImageClearHelper?
    fun clearRecyclerView() {
        logd("clearRecyclerView")
        imageClearHelper?.clear()
        imageClearHelper = null
        recyclerView?.adapter = null
        recyclerView?.layoutManager = null
        recyclerView = null
    }
}