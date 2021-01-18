package com.bytebyte6.data

import com.bytebyte6.base.logd
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.data.work.SearchImageImpl


class TvLogoSearch(private val dao: TvDao) : SearchImageImpl() {
    fun doThatShit() {
        dao.getTvs()
            .filter {
                it.logo.isEmpty()
            }
            .forEach {
                if (it.name.isNotEmpty()) {
                    val logo = search(
                        it.name.replace("&"," ")
                    )
                    if (logo.isNotEmpty()) {
                        it.logo = logo
                        dao.insert(it)
                    }
                }
            }
    }
}