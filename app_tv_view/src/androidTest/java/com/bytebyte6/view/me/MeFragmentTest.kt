package com.bytebyte6.view.me

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.bytebyte6.view.R
import com.bytebyte6.view.download.DownloadViewHolder
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MeFragmentTest {

    @Test
    fun test_long_click(){
        setup()
        onView(withId(R.id.lavEmpty))
            .withFailureHandler { _, _ ->
                //有数据测试长按是否正常
                onView(withId(R.id.recyclerView))
                    .perform(
                        RecyclerViewActions.actionOnItemAtPosition<DownloadViewHolder>(
                            0,
                            ViewActions.longClick()
                        )
                    )
                onView(withId(R.id.fab))
                    .check(matches(isDisplayed()))
            }
            .check(matches(isDisplayed()))
    }

    @Test
    fun test_title_is_display() {
        setup()
        onView(withText(R.string.me_playlist)).check(matches(isDisplayed()))
        onView(withText(R.string.me_import_file)).check(matches(isDisplayed()))
    }

    @Test
    fun test_menu_is_open(){
        setup()
        onView(withContentDescription(R.string.toolbar_navigation)).check(matches(isClickable()))
    }

    @Test
    fun test_import_is_open(){
        setup()
        onView(withContentDescription(R.string.m3u)).check(matches(isClickable()))
    }

    private fun setup() {
        val scenario = launchFragmentInContainer<MeFragment>(themeResId = R.style.Theme_Rtmp)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }
}