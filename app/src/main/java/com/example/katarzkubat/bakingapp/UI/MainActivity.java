package com.example.katarzkubat.bakingapp.UI;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.katarzkubat.bakingapp.Adapters.RecipesAdapter;
import com.example.katarzkubat.bakingapp.IdlingResource.SimpleIdlingResource;
import com.example.katarzkubat.bakingapp.Model.Recipes;
import com.example.katarzkubat.bakingapp.R;
import com.example.katarzkubat.bakingapp.Utilities.NetworkUtils;
import com.example.katarzkubat.bakingapp.Utilities.OpenRecipeJsonUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private RecipesAdapter recipesAdapter;
    ArrayList<Recipes> recipes;
    public final static String SINGLE_RECIPE = "singleRecipe";
    int widgetPosition;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @BindView(R.id.recipes_recycler) RecyclerView recipesRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SimpleIdlingResource)getIdlingResource()).setIdleState(false);

        setContentView(R.layout.activity_main);
        widgetPosition = getIntent().getIntExtra("widgetPosition", -1);

        ButterKnife.bind(this);

        if (findViewById(R.id.phone_layout) != null) {
            recipesRecycler.setLayoutManager(new LinearLayoutManager(this));

        } else {

            GridLayoutManager layoutManager = new GridLayoutManager
                    (this, 2, GridLayoutManager.VERTICAL, false);
            recipesRecycler.setLayoutManager(layoutManager);

            recipesRecycler.setHasFixedSize(true);
        }

        recipesAdapter = new RecipesAdapter(this);

        recipesRecycler.setAdapter(recipesAdapter);

        new PopulateRecipes().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class PopulateRecipes extends AsyncTask<String, Void, ArrayList<Recipes>> {

        @Override
        protected ArrayList<Recipes> doInBackground(String... strings) {

            URL recipeRequestUrl = null;

            try {
                recipeRequestUrl = NetworkUtils.buildRecipeUrl();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(recipeRequestUrl);

                return OpenRecipeJsonUtils
                        .getRecipesFromJson(jsonResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Recipes> recipes) {
            if (recipes!= null) {
                recipesAdapter.setRecipes(recipes);
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((SimpleIdlingResource)getIdlingResource()).setIdleState(true);
                }
            }, 3000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new PopulateRecipes().execute();
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
