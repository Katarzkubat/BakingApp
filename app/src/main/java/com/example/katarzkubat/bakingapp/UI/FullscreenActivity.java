package com.example.katarzkubat.bakingapp.UI;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.katarzkubat.bakingapp.Model.Recipes;
import com.example.katarzkubat.bakingapp.Model.Steps;
import com.example.katarzkubat.bakingapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullscreenActivity extends AppCompatActivity {

    private SimpleExoPlayer mExoPlayer;
    private View mContentView;
    private long movieCurrentPosition;
    private int position;
    private ArrayList<Steps> steps;
    private boolean twoPane;
    private Recipes singleRecipe;
    public static final String CURRENT_POSITION = "movieCurrentPosition";
    public static final String CHOSEN_STEP_POSITION = "position";
    public static final String STEPS = "steps";
    public static final String CURRENT_STEP = "currentStep";
    public final static String TWO_PANE = "twoPane";
    public final static String SINGLE_RECIPE = "singleRecipe";

    @BindView(R.id.step_details_player_fullscreen)
    SimpleExoPlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Intent getMovieIntent = getIntent();
        steps = getMovieIntent.getParcelableArrayListExtra(STEPS);
        Uri stepUri = Uri.parse(getMovieIntent
                .getStringExtra(CURRENT_STEP));

        movieCurrentPosition = getMovieIntent.getLongExtra(CURRENT_POSITION, 0);
        position = getMovieIntent.getIntExtra(CHOSEN_STEP_POSITION, 0);
        twoPane = getMovieIntent.getBooleanExtra(TWO_PANE, false);
        singleRecipe = getMovieIntent.getParcelableExtra(SINGLE_RECIPE);

        ButterKnife.bind(this);

        initializePlayer(stepUri);
    }

    private void initializePlayer(Uri stepUri) {

        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(this, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(stepUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(movieCurrentPosition);
            mExoPlayer.setPlayWhenReady(true);

            mPlayerView.findViewById(R.id.exo_fullscreen_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieCurrentPosition = mExoPlayer.getCurrentPosition();

                    Intent screenBack;
                    if(twoPane) {
                         screenBack = new Intent(FullscreenActivity.this, RecipeDetailsActivity.class);
                    }else {
                        screenBack = new Intent(FullscreenActivity.this, StepDetailedActivity.class);
                    }
                    screenBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    screenBack.putExtra(CURRENT_POSITION, movieCurrentPosition);
                    screenBack.putExtra(CHOSEN_STEP_POSITION, position);
                    screenBack.putParcelableArrayListExtra(STEPS, steps);
                    screenBack.putExtra(SINGLE_RECIPE, singleRecipe);
                    startActivity(screenBack);
                }
            });
        }
    }
}
