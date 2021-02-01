package com.bytebyte6.view;

import android.graphics.Rect;
import android.view.View;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.remote.annotation.RemoteMsgConstructor;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;

public class NotDisplayed extends TypeSafeMatcher<View> {
    @Override
    public void describeTo(Description description) {
        description.appendText("is displayed on the screen to the user");
    }

    @Override
    public boolean matchesSafely(View view) {
        return view.getGlobalVisibleRect(new Rect())
                && withEffectiveVisibility(ViewMatchers.Visibility.GONE).matches(view);
    }
}
