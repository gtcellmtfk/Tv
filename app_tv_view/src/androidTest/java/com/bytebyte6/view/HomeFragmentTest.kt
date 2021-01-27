package com.bytebyte6.view

import android.view.View
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.bytebyte6.view.home.HomeFragment
import com.google.android.material.tabs.TabLayout
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.allOf
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeFragmentTest {
    @Test
    fun test_title_is_display() {
        setup()
        onView(
            allOf(
                CoreMatchers.instanceOf(TextView::class.java),
                withParent(withId(R.id.toolbar))
            )
        )
            .check(ViewAssertions.matches(withText(R.string.home_category_view)))
    }

    @Test
    fun test_tab_is_display0(){
        setup()
        onView(withId(R.id.tabLayout)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.tabLayout)).perform(selectTabAtPosition(0))
    }

    @Test
    fun test_tab_is_display1(){
        setup()
        onView(withId(R.id.tabLayout)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.tabLayout)).perform(selectTabAtPosition(1))
    }

    @Test
    fun test_tab_is_display2(){
        setup()
        onView(withId(R.id.tabLayout)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.tabLayout)).perform(selectTabAtPosition(2))
    }

    private fun selectTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "tabLayoutContentDescription"

            override fun getConstraints() = allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))

            override fun perform(uiController: UiController, view: View) {
                val tabLayout = view as TabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                    ?: throw PerformException.Builder()
                        .withCause(Throwable("No tab at index $tabIndex"))
                        .build()

                tabAtIndex.select()
            }
        }
    }

    @Test
    fun test_menu_is_open(){
        setup()
        onView(withContentDescription(R.string.toolbar_navigation)).check(ViewAssertions.matches(isClickable()))
    }

    @Test
    fun test_search_is_open(){
        setup()
        onView(withContentDescription(R.string.search)).check(ViewAssertions.matches(isClickable()))
    }

    @Test
    fun test_share_is_open(){
        setup()
        onView(withContentDescription(R.string.share)).check(ViewAssertions.matches(isClickable()))
    }

    private fun setup() {
        val scenario = launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_Rtmp)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }
}