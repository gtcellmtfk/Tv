package com.bytebyte6.view.test

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.bytebyte6.view.R
import com.google.android.material.checkbox.MaterialCheckBox
import org.hamcrest.Matcher

object ClickFavorite : ViewAction {
    override fun getDescription(): String {
        return "click favorite"
    }

    override fun getConstraints(): Matcher<View> {
        return ViewMatchers.withId(R.id.button)
    }

    override fun perform(uiController: UiController, view: View) {
        val fav = view.findViewById<MaterialCheckBox>(R.id.button)
        val first = fav.isChecked
        fav.performClick()
        val second = fav.isChecked
        assert(first != second)
    }
}