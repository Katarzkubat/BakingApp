package com.example.katarzkubat.bakingapp.Utilities;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.katarzkubat.bakingapp.CakeWidgetProvider;
import com.example.katarzkubat.bakingapp.Model.Ingredients;
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

        ArrayList<Ingredients> ingredients = new ArrayList<Ingredients>();
        Recipes singleRecipe;
        private Context mContext;
        int widgetId;

        public RemoteListViewsFactory(Context context, Intent intent) {
            mContext = context;
            Log.d("RemoteListViewsFactory", "intent: "+intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,-1));

        }

        @Override
        public void onCreate() {
           // SharedPreferences sharedPref = mContext.getSharedPreferences();
           // int widgetChosenPosition = sharedPref.getInt(getString(R.string.widget_chosen_position), 0);
        }

        @Override
        public RemoteViews getViewAt(int position) {

            ingredients = singleRecipe.getIngredients();

            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.cake_widget_layout);
            remoteViews.setTextViewText(R.id.widget_ingredient_item_label, ingredients.get(position).getIngredient());

            return remoteViews;
        }

        @Override
        public void onDataSetChanged() {

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


