package com.example.katarzkubat.bakingapp.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.katarzkubat.bakingapp.Adapters.IngredientsAdapter;
import com.example.katarzkubat.bakingapp.Model.Ingredients;
import com.example.katarzkubat.bakingapp.Model.Recipes;
import com.example.katarzkubat.bakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class IngredientsFragment extends Fragment {

    IngredientsAdapter ingredientsAdapter;
    ArrayList<Ingredients> ingredients;
    Recipes recipes;
    public final static String SINGLE_RECIPE = "singleRecipe";
    @BindView(R.id.ingredients_recycler) RecyclerView ingredientsRecycler;
    @BindView(R.id.recipe_label) TextView recipeLabel;

    public IngredientsFragment() {}

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recipe_details_ingredients_fragment, container, false);
        Unbinder unbinder = ButterKnife.bind(this, rootView);

        recipes = getArguments().getParcelable(SINGLE_RECIPE);

        assert recipes != null;
        ingredients = recipes.getIngredients();

        recipeLabel.setText(recipes.getName());

        ingredientsAdapter = new IngredientsAdapter(getContext(), ingredients);

        ingredientsRecycler.setAdapter(ingredientsAdapter);

        ingredientsAdapter.setIngredients(ingredients);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        ingredientsAdapter.setIngredients(recipes.getIngredients());
    }

}

