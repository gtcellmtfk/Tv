package com.bytebyte6.view.test

import androidx.core.view.isVisible
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.bytebyte6.view.R
import com.bytebyte6.view.adapter.ImageViewHolder
import com.bytebyte6.view.favorite.FavoriteFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class FavoriteFragmentTest {

    @Test
    fun test_fav_is_not_display() {
        setup()
        onView(withId(R.id.recyclerview)).check { view, _ ->
            val r = view as RecyclerView
            if (r.adapter!!.itemCount != 0) {
                val item0 = r.findViewHolderForAdapterPosition(0) as ImageViewHolder
                assert(!item0.binding.button.isVisible)
            }
        }
    }

    @Test
    fun test_title_is_display() {
        setup()
        onView(withText(R.string.nav_fav)).check(matches(isDisplayed()))
    }

    @Test
    fun test_menu_is_open() {
        setup()
        onView(withContentDescription(R.string.toolbar_navigation)).check(matches(isClickable()))
    }

    private fun setup() {
        val scenario = launchFragmentInContainer<FavoriteFragment>(themeResId = R.style.Theme_Rtmp)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }
}