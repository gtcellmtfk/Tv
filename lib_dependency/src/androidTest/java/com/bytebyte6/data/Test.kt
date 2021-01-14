package com.bytebyte6.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.base.EventObserver
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class Test : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {

    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {

    }

    @Test
    fun testEventLiveData() {
        val eventLiveData = EventLiveData<String>()
        eventLiveData.postEventValue("123")
        eventLiveData.liveData().observeForever(EventObserver{
            println(it)
        })
    }

}