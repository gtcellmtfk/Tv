package com.bytebyte6.view.test

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.bytebyte6.view.KEY_TITLE
import com.bytebyte6.view.NotDisplayed
import com.bytebyte6.view.R
import com.bytebyte6.view.adapter.ImageViewHolder
import com.bytebyte6.view.videolist.VideoListFragment
import com.google.android.material.checkbox.MaterialCheckBox
import org.hamcrest.Matcher
import org.hamcrest.core.StringContains
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class VideoListFragmentTest {

    @Test
    fun test() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        setup()
        // 标题是否显示
        // 副标题是否显示
        // 列表是否显示
        // 收藏功能是否正常
        Espresso.onView(withText("Unkown")).check(matches(isDisplayed()))
        val subTitle = context.getString(R.string.total).split(" ")[0]
        Espresso.onView(withText(StringContains.containsString(subTitle))).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.emptyBox)).check(matches(NotDisplayed()))
        Espresso.onView(withId(R.id.recyclerview)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.recyclerview)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageViewHolder>(0, ClickFavorite)
        )
    }

    private fun setup() {
        val scenario = launchFragmentInContainer<VideoListFragment>(
            themeResId = R.style.Theme_Rtmp,
            fragmentArgs = Bundle().apply {
                putString(KEY_TITLE, "Unkown")
            }
        )
        scenario.moveToState(Lifecycle.State.RESUMED)
    }
}