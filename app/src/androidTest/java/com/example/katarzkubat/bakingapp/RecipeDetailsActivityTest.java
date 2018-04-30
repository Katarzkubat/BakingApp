package com.example.katarzkubat.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.katarzkubat.bakingapp.Model.Ingredients;
import com.example.katarzkubat.bakingapp.Model.Recipes;
import com.example.katarzkubat.bakingapp.Model.Steps;
import com.example.katarzkubat.bakingapp.UI.RecipeDetailsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailsActivityTest {

    private static final String STEP_NAME = "Recipe Introduction";

    @Rule
    public ActivityTestRule<RecipeDetailsActivity> recipeDetailsActivityTestRule =
            new ActivityTestRule<RecipeDetailsActivity>(RecipeDetailsActivity.class) {

                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, RecipeDetailsActivity.class);
                    final Ingredients ingredients = new Ingredients(1.4, "cup", "salt");
                    final Steps steps = new Steps("Recipe Introduction", 0, "testDesc", null, null);
                    Recipes singleRecipe = new Recipes(0, "recipeTest", new ArrayList<Ingredients>(){{add(ingredients);}}, new ArrayList<Steps>(){{add(steps);}}, 8, null);
                    result.putExtra("singleRecipe", singleRecipe);
                    return result;
                }
            };


   @Test
    public void Labels_DisplayText() {

        onView(withId(R.id.recipe_label)).check(matches(withText("recipeTest")));

        onView(withId(R.id.detail_step_label)).check(matches((isDisplayed())));

        onView(withId(R.id.detail_step_label)).check(matches(withText(STEP_NAME)));

        onView(withId(R.id.recipe_steps_recycler)).check(matches((isDisplayed())));
    }

   @Test
    public void ItemClicked_OpenStepDetail() {
        onView(withId(R.id.recipe_steps_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test
    public void MenuTest_GoHome() {

        onView(withId(R.id.home))
              .check(matches(withText("Home")));

        onView(withId(R.id.home))
                .perform(click());
    }
}
