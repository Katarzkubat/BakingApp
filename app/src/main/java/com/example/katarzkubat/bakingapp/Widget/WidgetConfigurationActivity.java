package com.example.katarzkubat.bakingapp.Widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.katarzkubat.bakingapp.Model.Recipes;
import com.example.katarzkubat.bakingapp.R;
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
    private int widgetId;
    Context context;
    private ArrayList<Recipes> recipes;

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

        recipes = new ArrayList<>();
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

            if (recipes != null) {
                int widgetChosenPosition = PreferenceManager
                        .getDefaultSharedPreferences(WidgetConfigurationActivity.this)
                        .getInt("widgetChosenPosition", -1);

                setRecipes(recipes);
                RadioGroup radioGroup = findViewById(R.id.recipes_radio_buttons);
                for (int position = 0; position < recipes.size(); position++) {

                    RadioButton radioButton = new RadioButton(WidgetConfigurationActivity.this);
                    radioButton.setId(position);
                    radioButton.setText(recipes.get(position).getName());

                    if(widgetChosenPosition == position){
                        radioButton.setChecked(true);
                    }

                    radioGroup.addView(radioButton);
                }

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        chooseRecipe(checkedId);
                    }
                });
            }
        }
    }

    private void setRecipes(ArrayList<Recipes> queredData){
        recipes = queredData;
    }

    private void chooseRecipe(int position){

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.widget_list);
        appWidgetManager.updateAppWidget(widgetId,
                CakeWidgetProvider.buildRemoteViews(getApplicationContext(),
                        widgetId));

        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putInt("widgetChosenPosition", position).apply();

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(RESULT_OK, resultValue);

        finish();

    }
}
