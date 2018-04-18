package com.example.katarzkubat.bakingapp.Utilities;

import android.util.Log;

import com.example.katarzkubat.bakingapp.Model.Ingredients;
import com.example.katarzkubat.bakingapp.Model.Recipes;
import com.example.katarzkubat.bakingapp.Model.Steps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;


public class OpenRecipeJsonUtils {

    public static ArrayList<Recipes> getRecipesFromJson(String jsonRecipes) {

        ArrayList<Recipes> recipesArrayList;

        Gson gson = new Gson();

        Type collectionType = new TypeToken<ArrayList<Recipes>>() {}.getType();

        recipesArrayList = new ArrayList<>(Arrays.asList(gson.fromJson(jsonRecipes, Recipes[].class)));
        return recipesArrayList;
    }
}
