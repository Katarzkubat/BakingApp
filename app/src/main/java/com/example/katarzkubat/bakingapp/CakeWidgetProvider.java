package com.example.katarzkubat.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.katarzkubat.bakingapp.UI.MainActivity;
import com.example.katarzkubat.bakingapp.UI.RecipeDetailsActivity;
import com.example.katarzkubat.bakingapp.Utilities.RemoteListService;

import java.util.Objects;


public class CakeWidgetProvider extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cake_widget);

            Intent titleIntent = new Intent(context, MainActivity.class);

            PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_title_label, titlePendingIntent);

            Intent intent = new Intent(context, RemoteListService.class);
            views.setRemoteAdapter(R.id.widget_list, intent);

            Intent ingredientsIntent = new Intent(context, MainActivity.class);
            PendingIntent ingredientsPendingIntent = PendingIntent
                    .getActivity(context, 10, ingredientsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_list, ingredientsPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {}

    @Override
    public void onDisabled(Context context) {}

    public static void sendRefreshBroadcast(Context context) {

        Intent brIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        brIntent.setComponent(new ComponentName(context, CakeWidgetProvider.class));
        context.sendBroadcast(brIntent);
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager manager = AppWidgetManager.getInstance(context);

        if (Objects.equals(intent.getAction(), AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            ComponentName componentName = new ComponentName(context, CakeWidgetProvider.class);
            manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(componentName), R.id.widget_list);
        }

        super.onReceive(context, intent);
    }

}

