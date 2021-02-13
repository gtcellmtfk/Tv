package com.bytebyte6.view.test

import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.bytebyte6.view.R
import com.bytebyte6.view.download.DownloadFragment
import com.bytebyte6.view.download.DownloadViewHolder
import org.hamcrest.CoreMatchers
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class DownloadFragmentTest {

    @Test
    fun empty_view_is_display() {
        setup()

        Espresso.onView(ViewMatchers.withId(R.id.emptyBox))
            .withFailureHandler { _, _ ->
                //有数据测试长按是否正常
                Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                    .perform(
                        RecyclerViewActions.actionOnItemAtPosition<DownloadViewHolder>(
                            0,
                            ViewActions.longClick()
                        )
                    )
                Espresso.onView(ViewMatchers.withText(R.string.cancel))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            }
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    @Test
    fun test_overflow_is_display() {
        setup()
        Espresso.openContextualActionModeOverflowMenu()
        Espresso.onView(ViewMatchers.withText(R.string.pause))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(R.string.resume))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_title_is_display() {
        setup()
        Espresso.onView(
            CoreMatchers.allOf(
                CoreMatchers.instanceOf(TextView::class.java),
                ViewMatchers.withParent(ViewMatchers.withId(R.id.toolbar))
            )
        )
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.nav_download)))
    }

    @Test
    fun test_menu_is_open() {
        setup()
        Espresso.onView(ViewMatchers.withContentDescription(R.string.toolbar_navigation))
            .check(ViewAssertions.matches(ViewMatchers.isClickable()))
    }

    private fun setup() {
        val scenario = launchFragmentInContainer<DownloadFragment>(themeResId = R.style.Theme_Rtmp)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }
}