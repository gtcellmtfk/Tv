package com.bytebyte6.usecase.work

import com.bytebyte6.common.logd
import com.bytebyte6.data.DataManager
import com.bytebyte6.image.SearchImage

class SearchTvLogo(private val dataManager: DataManager, private val searchImage: SearchImage) {
    fun searchLogo() {
        dataManager.getLogoEmptyTvs().forEach {
            val logo = searchImage.search(it.name)
            if (logo.isNotEmpty()) {
                it.logo = logo
                logd("logo=$logo")
                dataManager.updateTv(it)
            }
        }
    }
}