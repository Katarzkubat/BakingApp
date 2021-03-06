package com.example.katarzkubat.bakingapp.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.katarzkubat.bakingapp.Model.Steps;
import com.example.katarzkubat.bakingapp.R;

import java.util.ArrayList;

public class StepDetailedActivity extends AppCompatActivity {

    private static final String CURRENT_POSITION = "movieCurrentPosition";
    private static final String CHOSEN_STEP_POSITION = "position";
    private static final String STEPS = "steps";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detailed);
        if (savedInstanceState == null) {
            Intent stepIntent = getIntent();
            ArrayList<Steps> steps = stepIntent.getParcelableArrayListExtra(STEPS);
            int position = stepIntent.getIntExtra(CHOSEN_STEP_POSITION, 0);
            long movieCurrentPosition = stepIntent.getLongExtra(CURRENT_POSITION, 0);

            Bundle arguments = new Bundle();
            arguments.putInt(CHOSEN_STEP_POSITION, position);
            arguments.putParcelableArrayList(STEPS, steps);
            arguments.putLong(CURRENT_POSITION, movieCurrentPosition);

            StepDetailedFragment stepDetailedFragment = new StepDetailedFragment();
            stepDetailedFragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detailed_container, stepDetailedFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent homeActivity = new Intent(StepDetailedActivity.this, MainActivity.class);
            startActivity(homeActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
