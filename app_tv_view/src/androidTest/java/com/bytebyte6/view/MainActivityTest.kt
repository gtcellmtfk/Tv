package com.bytebyte6.view

import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {
    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun test_title_is_display() {
        onView(
            CoreMatchers.allOf(
                CoreMatchers.instanceOf(TextView::class.java),
                withParent(withId(R.id.toolbar))
            )
        )
            .check(matches(withText(R.string.home_category_view)))
    }

    @Test
    fun test_menu_is_open() {
        try {
            onView(withContentDescription(R.string.toolbar_navigation)).perform(ViewActions.click())
            onView(withId(R.id.tvName)).check(matches(isDisplayed()))
        } catch (e: Exception) {
            System.err.println(e.message)
        }
    }

    @Test
    fun test_search_is_open() {
        onView(withContentDescription(R.string.search)).perform(ViewActions.click())
        onView(withId(R.id.lavEmpty)).check(matches(isDisplayed()))
    }
}