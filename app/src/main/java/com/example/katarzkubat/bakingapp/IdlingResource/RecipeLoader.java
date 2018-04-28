package com.example.katarzkubat.bakingapp.IdlingResource;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.example.katarzkubat.bakingapp.Model.Recipes;

import java.util.ArrayList;

public class RecipeLoader {

    private static final int DELAY_MILLIS = 3000;

    final static ArrayList<Recipes> recipes = new ArrayList<>();

    public interface RecipeCallback {
        void onDone(ArrayList<Recipes> recipes);
    }

    static void downloadRecipes(Context context, final RecipeCallback callback,
                                @Nullable final SimpleIdlingResource idlingResource) {

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onDone(recipes);
                    if (idlingResource != null) {
                        idlingResource.setIdleState(true);
                    }
                }
            }
        }, DELAY_MILLIS);
    }
}


