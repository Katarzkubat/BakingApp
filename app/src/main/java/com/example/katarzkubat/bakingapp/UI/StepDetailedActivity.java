package com.example.katarzkubat.bakingapp.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.katarzkubat.bakingapp.Model.Steps;
import com.example.katarzkubat.bakingapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import android.support.v4.media.session.MediaSessionCompat;
import android.widget.Toast;

import java.util.ArrayList;

public class StepDetailedActivity extends AppCompatActivity {

    public static final String CURRENT_POSITION = "movieCurrentPosition";
    public static final String CHOSEN_STEP_POSITION = "position";
    public static final String STEPS = "steps";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detailed);

        Intent stepIntent = getIntent();
        ArrayList<Steps> steps = stepIntent.getParcelableArrayListExtra(STEPS);
        int position = stepIntent.getIntExtra(CHOSEN_STEP_POSITION, 0);
        long movieCurrentPosition = stepIntent.getLongExtra(CURRENT_POSITION, 0);

        //BUNDLE Z POZYCJĄ FILMU DO LANDSCAPE -po obrocie wraca do 0.00

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
