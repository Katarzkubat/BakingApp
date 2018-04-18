package com.example.katarzkubat.bakingapp.Utilities;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.katarzkubat.bakingapp.CakeWidgetProvider;
import com.example.katarzkubat.bakingapp.Model.Recipes;
import com.example.katarzkubat.bakingapp.R;
import com.example.katarzkubat.bakingapp.UI.MainActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class RemoteListService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteListViewsFactory(this.getApplicationContext(), intent);
    }

    public class RemoteListViewsFactory implements RemoteViewsService.RemoteViewsFactory {

       ArrayList<Recipes> recipes = new ArrayList<Recipes>();

        private Context mContext;

        public RemoteListViewsFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.cake_widget_list_item);
            remoteViews.setTextViewText(R.id.widget_recipe_item_label, recipes.get(position).getName());

            Intent fillInIntent = new Intent();
            fillInIntent.putExtra("widgetPosition", position);
            remoteViews.setOnClickFillInIntent(R.id.widgetItemContainer, fillInIntent);

            return remoteViews;
        }

        @Override
        public void onDataSetChanged() {
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

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return recipes.size();
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


