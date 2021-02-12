package com.bytebyte6.usecase.work

import com.bytebyte6.data.DataManager
import com.bytebyte6.image.SearchImage

class SearchTvLogo(private val dataManager: DataManager, private val searchImage: SearchImage) {
    fun searchLogo() {
        dataManager.getLogoEmptyTvs().forEach {
            val logo = searchImage.search(it.name.replace("&", " "))
            if (logo.isNotEmpty()) {
                it.logo = logo
                dataManager.updateTv(it)
            }
        }
    }
}