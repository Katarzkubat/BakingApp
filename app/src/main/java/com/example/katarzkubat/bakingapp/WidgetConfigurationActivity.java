package com.example.katarzkubat.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;

import com.example.katarzkubat.bakingapp.Adapters.IngredientsAdapter;
import com.example.katarzkubat.bakingapp.Model.Recipes;
import com.example.katarzkubat.bakingapp.UI.MainActivity;
import com.example.katarzkubat.bakingapp.Utilities.NetworkUtils;
import com.example.katarzkubat.bakingapp.Utilities.OpenRecipeJsonUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WidgetConfigurationActivity extends AppCompatActivity {

    @BindView(R.id.recipes_radio_buttons)
    RadioGroup radioGroup;

    public final static String EXTRA_APPWIDGET_ID = "widgetId";
    int widgetId;
    Context context;
    ArrayList<Recipes> recipes;
    Recipes singleRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_widget_configuration);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        recipes = new ArrayList<Recipes>();

        Log.d("WIDGET", "widget " + widgetId);

        new PopulateRecipes().execute();
        Log.d("WIDGET", "populaateRecipe");
    }

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

            if (recipes != null) {
                setRecipes(recipes);
                RadioGroup radioGroup = findViewById(R.id.recipes_radio_buttons);
                for (int position = 0; position < recipes.size(); position++) {

                    RadioButton rdbtn = new RadioButton(WidgetConfigurationActivity.this);
                    rdbtn.setId(position);
                    rdbtn.setText(recipes.get(position).getName());
                    radioGroup.addView(rdbtn);
                }

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        Log.d("onCheckedChanged", "checkedId: "+checkedId);
                        chooseRecipe(checkedId);
                    }
                });
            }
        }
    }

    public void setRecipes(ArrayList<Recipes> queredData){
        recipes = queredData;
    }

    public void chooseRecipe(int position){
        singleRecipe = recipes.get(position);

        RemoteViews views = new RemoteViews(getPackageName(), R.layout.cake_widget_layout);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.widget_list);
        appWidgetManager.updateAppWidget(widgetId,
                CakeWidgetProvider.buildRemoteViews(getApplicationContext(),
                        widgetId, singleRecipe));

       /* SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.widget_chosen_position), position);
        editor.commit(); */

        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putInt("widgetChosenPosition", position).commit();

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(RESULT_OK, resultValue);

        finish();

    }
}
