package com.bytebyte6.library

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        val adapterHelper2 = AdapterHelper2Impl<String, DetailsViewHolder>()
        adapterHelper2.onBind = { pos, view ->

        }
        assert(adapterHelper2.onBind==null)
    }
}