package com.example.katarzkubat.bakingapp.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.katarzkubat.bakingapp.Model.Ingredients;
import com.example.katarzkubat.bakingapp.Model.Recipes;
import com.example.katarzkubat.bakingapp.R;
import com.example.katarzkubat.bakingapp.Utilities.NetworkUtils;
import com.example.katarzkubat.bakingapp.Utilities.OpenRecipeJsonUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RemoteListService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteListViewsFactory(this.getApplicationContext(), intent);
    }

    public class RemoteListViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private ArrayList<Ingredients> ingredients = new ArrayList<>();
        ArrayList<Recipes> recipes = new ArrayList<>();
        private final Context mContext;
        int widgetId;
        int widgetChosenPosition;

        RemoteListViewsFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public void onCreate() {

            widgetChosenPosition = PreferenceManager
                    .getDefaultSharedPreferences(mContext).getInt("widgetChosenPosition", 0);

        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.cake_widget_list_item);
            remoteViews.setTextViewText(R.id.widget_ingredient_item_label, ingredients.get(position).getIngredient());

            return remoteViews;
        }

        @Override
        public void onDataSetChanged() {
            widgetChosenPosition = PreferenceManager
                    .getDefaultSharedPreferences(mContext).getInt("widgetChosenPosition", 0);
            URL recipeRequestUrl = null;

            try {
                recipeRequestUrl = NetworkUtils.buildRecipeUrl();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(recipeRequestUrl);
                recipes = OpenRecipeJsonUtils
                        .getRecipesFromJson(jsonResponse);
                ingredients = recipes.get(widgetChosenPosition).getIngredients();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return ingredients.size();
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}


