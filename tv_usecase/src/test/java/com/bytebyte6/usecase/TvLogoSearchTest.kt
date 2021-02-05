package com.bytebyte6.usecase

import com.bytebyte6.data.entity.Tv
import com.bytebyte6.image.SearchImageImpl
import com.bytebyte6.testdata.tvs
import com.bytebyte6.usecase.work.TvLogoSearch
import org.junit.Test

class TvLogoSearchTest {
    @Test
    fun test() {
        val dataManager = object : com.bytebyte6.testdata.TestDataManager() {
            override fun getTvs(): List<Tv> {
                return tvs.apply {
                    this[0].logo = "aa"
                }
            }

            val ups = mutableListOf<Tv>()

            override fun updateTv(tvs: List<Tv>) {
                ups.addAll(tvs)
            }
        }
        val search = TvLogoSearch(dataManager, SearchImageImpl())
        search.doThatShit()
        assert(dataManager.ups.size == tvs.size - 1)
        dataManager.ups.forEach {
            assert(it.logo.isNotEmpty())
        }
    }
}