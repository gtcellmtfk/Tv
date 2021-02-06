package com.bytebyte6.usecase.work

import com.bytebyte6.common.logd
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.image.SearchImage

class SearchTvLogo(private val dataManager: DataManager, private val searchImage: SearchImage) {
    fun searchLogo(list: List<Tv>? = null) {
        (list ?: dataManager.getTvs()).filter {
            it.logo.isEmpty() && it.name.isNotEmpty()
        }
            .forEach {
                val logo = searchImage.search(it.name)
                if (logo.isNotEmpty()) {
                    it.logo = logo
                    dataManager.updateTv(it)
                    logd("${it.name} ${it.logo}")
                }
            }
    }
}