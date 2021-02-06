package com.bytebyte6.view.test

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.bytebyte6.view.NotDisplayed
import com.bytebyte6.view.R
import com.bytebyte6.view.search.SearchFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchFragmentTest {

    @Test
    fun test_fav_is_not_display() {
        setup()
        onView(withId(R.id.lavEmpty)).check(matches(isDisplayed()))
        onView(withId(R.id.etSearch)).perform(ViewActions.replaceText("CCTV"))
        Thread.sleep(1000)
        onView(withId(R.id.lavEmpty)).check(matches(
            NotDisplayed()
        ))
    }

    @Test
    fun test_toolbar_navigation_is_clickable(){
        setup()
        onView(withContentDescription(R.string.toolbar_navigation)).check(matches(isClickable()))
    }

    private fun setup() {
        val scenario = launchFragmentInContainer<SearchFragment>(themeResId = R.style.Theme_Rtmp)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }
}