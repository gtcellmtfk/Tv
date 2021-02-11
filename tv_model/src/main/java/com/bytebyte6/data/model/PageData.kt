package com.bytebyte6.data.model

import com.bytebyte6.data.PAGE_SIZE

/**
 * 页码从0开始
 */
class PageData<T>(
    private val pageSize: Int = PAGE_SIZE,
    /**
     * 分页前的全部数据
     */
    all: List<T>
) {

    val count = all.size

    val pageCount = all.size.div(pageSize).plus(if (all.size % pageSize != 0) 1 else 0)

    /**
     * 分页后的数据
     */
    private val pagingData: MutableList<List<T>> = all.run {
        val result = mutableListOf<List<T>>()
        var fromIndex = 0
        for (page in 0 until pageCount) {
            val toIndex = fromIndex + pageSize
            result.add(subList(fromIndex, if (toIndex > count) count else toIndex))
            fromIndex += pageSize
        }
        result
    }

    /**
     * 获取分页数据
     */
    fun paging(page: Int): List<T> {
        return if (pagingData.isEmpty()) {
            emptyList()
        } else {
            if (page >= pageCount) {
                throw IllegalArgumentException("Requested pages are greater than total pages.")
            }
            pagingData[page]
        }
    }

    /**
     * 分页数据改变时调用
     */
    fun updatePaging(page: Int, list: List<T>) {
        val size = pagingData.size
        if (size == 0) {
            return
        }
        if (page >= pageCount) {
            throw IllegalArgumentException("Requested pages are greater than total pages.")
        }
        if (pagingData[page].size != list.size) {
            throw IllegalArgumentException("The new page data does not match the length of the old page data")
        }
        pagingData[page] = list
    }
}