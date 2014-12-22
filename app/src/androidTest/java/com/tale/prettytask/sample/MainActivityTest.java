package com.tale.prettytask.sample;

import android.test.ActivityInstrumentationTestCase2;

import com.google.android.apps.common.testing.ui.espresso.Espresso;
import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;
import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;

/**
 * Created by tale on 12/20/14.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {
        super("com.tale.prettytask.sample", MainActivity.class);
    }

    @Override public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testAsync() throws Exception {
        Espresso.onView(ViewMatchers.withId(R.id.tvMessage)).check(ViewAssertions.matches(ViewMatchers.withText("Success")));
    }
}
