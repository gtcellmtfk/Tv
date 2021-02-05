package com.bytebyte6.usecase.work

import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.image.SearchImage

class TvLogoSearch(private val dataManager: DataManager, private val searchImage: SearchImage) {
    fun doThatShit() {
        val list = mutableListOf<Tv>()
        dataManager.getTvs()
            .filter {
                it.logo.isEmpty() && it.name.isNotEmpty()
            }
            .forEach {
                val logo = searchImage.search(it.name)
                if (logo.isNotEmpty()) {
                    it.logo = logo
                    // 频繁更新数据库建议每10个更新一遍
                    // dataManager.updateTv(it)
                    list.add(it)
                }
                if (list.size == 10) {
                    dataManager.updateTv(list)
                    list.clear()
                }
            }
        if (list.isNotEmpty()) {
            dataManager.updateTv(list)
        }
    }
}