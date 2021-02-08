package com.bytebyte6.data

import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.model.PageData
import org.junit.Test

class PageDataTest {
    @Test
    fun test_page_data() {
        val cs = mutableListOf<Country>()
        for (i in 0..100) {
            cs.add(Country(name = "A $i"))
        }
        val pageData = PageData(all = cs)
        assert(pageData.count == 101)
        assert(pageData.paging(0).size == PAGE_SIZE)
        assert(pageData.pageCount == 6)
        assert(pageData.paging(5).size == 1)
    }
}