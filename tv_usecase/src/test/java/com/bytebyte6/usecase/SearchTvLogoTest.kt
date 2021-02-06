package com.bytebyte6.usecase

import com.bytebyte6.data.entity.Tv
import com.bytebyte6.image.SearchImageImpl
import com.bytebyte6.testdata.TestDataManager
import com.bytebyte6.testdata.tvs
import com.bytebyte6.usecase.work.SearchTvLogo
import org.junit.Test

class SearchTvLogoTest {
    @Test
    fun test() {
        val dataManager = object : TestDataManager() {
            override fun getTvs(): List<Tv> {
                return tvs.apply {
                    this[0].logo = "aa"
                }
            }

            val ups = mutableListOf<Tv>()

            override fun updateTv(tv:Tv) {
                ups.add(tv)
            }
        }
        val search = SearchTvLogo(dataManager, SearchImageImpl())
        search.searchLogo()
        assert(dataManager.ups.size == tvs.size - 1)
        dataManager.ups.forEach {
            assert(it.logo.isNotEmpty())
        }
    }
}