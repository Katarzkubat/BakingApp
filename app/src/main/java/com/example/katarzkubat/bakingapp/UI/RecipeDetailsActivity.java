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
import com.example.katarzkubat.bakingapp.R;

import java.util.ArrayList;

import static com.example.katarzkubat.bakingapp.Adapters.RecipesAdapter.SINGLE_RECIPE;

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeStepsFragment.OnItemClickListener {

    ArrayList<Recipes> recipes;
    ArrayList<Ingredients> ingredients;
    private boolean twoPane;
    public final static String TWO_PANE = "twoPane";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        if(savedInstanceState == null) {

            FragmentManager fragmentManager = getSupportFragmentManager();

            Recipes takenRecipe = getIntent().getParcelableExtra(SINGLE_RECIPE);
            int widgetPosition = getIntent().getIntExtra("widgetPosition", -1);

            Bundle bundle = new Bundle();

            bundle.putParcelable(SINGLE_RECIPE, takenRecipe);
            bundle.putBoolean(TWO_PANE, true);

            /*if(widgetPosition > -1) {
              findViewById(R.id.step_detailed_container).setVisibility(View.INVISIBLE);
              //findViewById(R.id.ingredients_fragment_container).setMinimumHeight(400);
            } */


            if(findViewById(R.id.two_pane_linear_layout) != null) {

                twoPane = true;

                IngredientsFragment ingredientsFragment = new IngredientsFragment();
                ingredientsFragment.setArguments(bundle);

                RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
                recipeStepsFragment.setArguments(bundle);

                StepDetailedFragment stepDetailedFragment = new StepDetailedFragment();
                stepDetailedFragment.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .add(R.id.ingredients_fragment_container, ingredientsFragment)
                        .add(R.id.steps_fragment_container, recipeStepsFragment)
                        .add(R.id.step_detailed_fragment, stepDetailedFragment)
                        .commit();
            } else {

                IngredientsFragment ingredientsFragment = new IngredientsFragment();
                ingredientsFragment.setArguments(bundle);

                RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
                recipeStepsFragment.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .add(R.id.ingredients_fragment_container, ingredientsFragment)
                        .add(R.id.steps_fragment_container, recipeStepsFragment)
                        .commit();
                twoPane = false;
            }
        }
    }

    @Override
    public void onItemSelected(int position) {}

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
