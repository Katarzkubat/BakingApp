package com.example.katarzkubat.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.katarzkubat.bakingapp.Model.Recipes;
import com.example.katarzkubat.bakingapp.UI.MainActivity;
import com.example.katarzkubat.bakingapp.Utilities.RemoteListService;

import java.util.Objects;


public class CakeWidgetProvider extends AppWidgetProvider {
    private static Recipes recipe;
    public static RemoteViews buildRemoteViews(Context context, int widgetId,Recipes singleRecipe) {
        recipe = singleRecipe;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cake_widget_layout);
        Intent intent = new Intent(context, RemoteListService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        intent.putExtra("singleRecipe", singleRecipe);

        views.setRemoteAdapter(R.id.widget_list, intent);

        Intent titleIntent = new Intent(context, MainActivity.class);

        PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
        views.setOnClickPendingIntent(R.id.widget_title_label, titlePendingIntent);

        //appWidgetManager.updateAppWidget(widgetId, views);


        return views;
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = buildRemoteViews(context, appWidgetId, CakeWidgetProvider.recipe);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            RemoteViews rv = buildRemoteViews(context, appWidgetId,CakeWidgetProvider.recipe);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
    }

    @Override
    public void onEnabled(Context context) {}

    @Override
    public void onDisabled(Context context) {}

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

    }
}

