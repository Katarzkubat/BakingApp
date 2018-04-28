package com.example.katarzkubat.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Root;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.WindowManager;

import com.example.katarzkubat.bakingapp.Model.Ingredients;
import com.example.katarzkubat.bakingapp.Model.Steps;
import com.example.katarzkubat.bakingapp.UI.StepDetailedActivity;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class StepDetailedActivityTest {

    @Rule
    public ActivityTestRule<StepDetailedActivity> stepDetailedActivityTestRule =
            new ActivityTestRule<StepDetailedActivity>(StepDetailedActivity.class) {

                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, StepDetailedActivity.class);
                    final Steps steps = new Steps("Recipe Introduction", 0, "testDesc", null, null);
                    result.putParcelableArrayListExtra("steps", new ArrayList<Steps>() {{
                        add(steps);
                    }});
                    result.putExtra("position", 0);
                    return result;
                }
            };

    @Test
    public void clickButton_OpenStep() {

        onView(withId(R.id.step_details_button_next))
                .check(matches(withText("Next")));
        onView(withId(R.id.step_details_button_next)).perform(click());

        onView(withId(R.id.step_details_button_previous))
                .check(matches(withText("Previous")));
        onView(withId(R.id.step_details_button_previous)).perform(click());
    }

    @Test
    public void MenuTest_GoHome() {

        onView(withId(R.id.home))
                .check(matches(withText("Home")));

        onView(withId(R.id.home))
                .perform(click());
    }

    @Test
    public void DescriptionViewTest_DisplayText() {

        onView(withId(R.id.step_details_description)).check(matches(withText("testDesc")));
    }

    public class ToastMatcher extends TypeSafeMatcher<Root> {

        @Override
        public void describeTo(Description description) {
            description.appendText("is toast");
        }

        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                if (windowToken == appToken) {
                    return true;
                }
            }
            return false;
        }
    }

    @Test
    public void Toast_ShowToast() {

        onView(withText(R.string.toast_msg)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }
}

