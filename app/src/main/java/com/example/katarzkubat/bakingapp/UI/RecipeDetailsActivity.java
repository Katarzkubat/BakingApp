package com.example.katarzkubat.bakingapp.UI;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.katarzkubat.bakingapp.Adapters.StepsAdapter;
import com.example.katarzkubat.bakingapp.Model.Ingredients;
import com.example.katarzkubat.bakingapp.Model.Recipes;
import com.example.katarzkubat.bakingapp.Model.Steps;
import com.example.katarzkubat.bakingapp.R;

import java.util.ArrayList;

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeStepsFragment.OnItemClickListener,
                                                                    StepDetailedFragment.OnPlayerPause {

    ArrayList<Recipes> recipes;
    ArrayList<Ingredients> ingredients;
    ArrayList<Steps> steps;
    private boolean twoPane = false;
    private Recipes singleRecipe;
    int chosenStepPosition;
    long movieCurrentPosition;
    public final static String TWO_PANE = "twoPane";
    public static final String CHOSEN_STEP_POSITION = "position";
    public static final String STEPS = "steps";
    public final static String SINGLE_RECIPE = "singleRecipe";
    public static final String CURRENT_POSITION = "movieCurrentPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        if(findViewById(R.id.two_pane_linear_layout) != null) {
            twoPane = true;
        }

        if(savedInstanceState == null) {
            singleRecipe = getIntent().getParcelableExtra(SINGLE_RECIPE);
            chosenStepPosition = getIntent().getIntExtra(CHOSEN_STEP_POSITION, -1);
            movieCurrentPosition = getIntent().getLongExtra(CURRENT_POSITION, 0);

        } else {

            singleRecipe = savedInstanceState.getParcelable(SINGLE_RECIPE);
            chosenStepPosition = savedInstanceState.getInt(CHOSEN_STEP_POSITION);
            movieCurrentPosition = savedInstanceState.getLong(CURRENT_POSITION);

        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        steps = singleRecipe.getSteps();

        Bundle bundle = new Bundle();

        bundle.putParcelable(SINGLE_RECIPE, singleRecipe);
        bundle.putBoolean(TWO_PANE, twoPane);

        if(twoPane) {

            bundle.putInt(CHOSEN_STEP_POSITION, chosenStepPosition);
            bundle.putLong(CURRENT_POSITION, movieCurrentPosition);

            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setArguments(bundle);

            RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
            recipeStepsFragment.setArguments(bundle);

            StepDetailedFragment stepDetailedFragment = new StepDetailedFragment();
            stepDetailedFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .replace(R.id.ingredients_fragment_container, ingredientsFragment)
                    .replace(R.id.steps_fragment_container, recipeStepsFragment)
                    .replace(R.id.step_detailed_fragment, stepDetailedFragment)
                    .commit();
        } else {

            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setArguments(bundle);

            RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
            recipeStepsFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .replace(R.id.ingredients_fragment_container, ingredientsFragment)
                    .replace(R.id.steps_fragment_container, recipeStepsFragment)
                    .commit();
        }
    }

    @Override
    public void setMovieCurrentPosition(long movieTime){
        movieCurrentPosition = movieTime;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(CHOSEN_STEP_POSITION, chosenStepPosition);
        outState.putParcelable(SINGLE_RECIPE, singleRecipe);
        outState.putLong(CURRENT_POSITION, movieCurrentPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(int position) {

        StepDetailedFragment stepDetailedFragment = new StepDetailedFragment();
        chosenStepPosition = position;
        if(twoPane) {

            Bundle bundle = new Bundle();
            bundle.putInt(CHOSEN_STEP_POSITION, position);
            bundle.putParcelableArrayList(STEPS, steps);
            bundle.putParcelable(SINGLE_RECIPE, singleRecipe);
            bundle.putBoolean(TWO_PANE, true);

            stepDetailedFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detailed_fragment, stepDetailedFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent homeActivity = new Intent(RecipeDetailsActivity.this, MainActivity.class);
            startActivity(homeActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
