package com.bytebyte6.usecase.work

import com.bytebyte6.data.DataManager
import com.bytebyte6.image.SearchImage

class TvLogoSearch(private val dataManager: DataManager, private val searchImage: SearchImage) {
    fun doThatShit() {
        dataManager.getTvs()
            .filter {
                it.logo.isEmpty()
            }
            .forEach {
                if (it.name.isNotEmpty()) {
                    val logo = searchImage.search(it.name)
                    if (logo.isNotEmpty()) {
                        it.logo = logo
                        dataManager.insertTv(it)
                    }
                }
            }
    }
}