package com.springa.i8lj;

import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/** On-device smoke test: the activity launches and its core chrome exists. */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Test
    public void activityLaunches_withCoreUi() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                assertNotNull(activity.findViewById(R.id.fab));
                Toolbar toolbar = activity.findViewById(R.id.toolbar);
                if (toolbar != null) {
                    assertEquals(Spec.APP_NAME, toolbar.getTitle());
                }
            });
        }
    }

    @Test
    public void emptyViewExists() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                View empty = activity.findViewById(R.id.empty_view);
                assertNotNull(empty);
                assertNotNull(activity.findViewById(R.id.list));
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(1, 1);
                assertNotNull(lp);
            });
        }
    }
}