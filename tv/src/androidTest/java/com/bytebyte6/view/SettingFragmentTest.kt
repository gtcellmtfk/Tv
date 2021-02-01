package com.bytebyte6.view

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.bytebyte6.view.setting.SettingFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SettingFragmentTest {

    @Test
    fun test_title_is_display() {
        setup()
        onView(withText(R.string.nav_setting)).check(matches(isDisplayed()))
    }

    @Test
    fun test_menu_is_open(){
        setup()
        onView(withContentDescription(R.string.toolbar_navigation)).check(matches(isClickable()))
    }

    @Test
    fun test_switch_is_clickable(){
        setup()
        onView(withId(R.id.swCapturePic)).check(matches(isClickable()))
        onView(withId(R.id.swNightMode)).check(matches(isClickable()))
    }

    @Test
    fun test_me_is_clickable(){
        setup()
        onView(withId(R.id.tvByteByte6)).check(matches(isClickable()))
    }

    private fun setup() {
        val scenario = launchFragmentInContainer<SettingFragment>(themeResId = R.style.Theme_Rtmp)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }
}