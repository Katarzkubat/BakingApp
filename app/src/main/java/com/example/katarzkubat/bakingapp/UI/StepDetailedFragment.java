package com.example.katarzkubat.bakingapp.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.katarzkubat.bakingapp.Model.Recipes;
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

import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StepDetailedFragment extends Fragment implements ExoPlayer.EventListener {

    private SimpleExoPlayer mExoPlayer;
    //private SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private static final String TAG = StepDetailedActivity.class.getSimpleName();
    private ArrayList<Steps> steps;
    private Steps currentStep;
    private long movieCurrentPosition;
    private int position;
    private boolean twoPane;
    private Recipes singleRecipe;
    private Uri stepUri;

    OnPlayerPause playerCallback;
    RecipeStepsFragment.OnItemClickListener mCallback;

    public static final String CURRENT_POSITION = "movieCurrentPosition";
    public static final String CHOSEN_STEP_POSITION = "position";
    public static final String STEPS = "steps";
    public static final String CURRENT_STEP = "currentStep";
    public final static String TWO_PANE = "twoPane";
    public final static String SINGLE_RECIPE = "singleRecipe";

    @BindView(R.id.step_details_button_next)
    Button nextButton;
    @BindView(R.id.step_details_button_previous)
    Button previousButton;
    @BindView(R.id.step_details_description)
    TextView description;
    @BindView(R.id.step_details_player)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.cake_image)
    ImageView image;
    @BindDrawable(R.drawable.cake)
    Drawable cakeDrawable;

    public StepDetailedFragment() {
    }

    public interface OnPlayerPause {
        void setMovieCurrentPosition(long movieTime);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.step_detailed_fragment, container, false);

        Unbinder unbinder = ButterKnife.bind(this, rootView);

        singleRecipe = getArguments().getParcelable(SINGLE_RECIPE);
        twoPane = getArguments().getBoolean(TWO_PANE);
        steps = getArguments().getParcelableArrayList(STEPS);

        if (twoPane) {
            position = 0;
            assert singleRecipe != null;
            steps = singleRecipe.getSteps();

            if (savedInstanceState != null && savedInstanceState.getInt(CHOSEN_STEP_POSITION) > -1) {
                position = savedInstanceState.getInt(CHOSEN_STEP_POSITION);
                savedInstanceState.putInt(CHOSEN_STEP_POSITION, -1);

            } else if (getArguments().getInt(CHOSEN_STEP_POSITION) > 0) {
                position = getArguments().getInt(CHOSEN_STEP_POSITION);
            }

        } else {
            position = getArguments().getInt(CHOSEN_STEP_POSITION);
            steps = getArguments().getParcelableArrayList(STEPS);
            assert steps != null;
        }

        currentStep = steps.get(position);

        if (savedInstanceState != null) {
            movieCurrentPosition = savedInstanceState.getLong(CURRENT_POSITION);

        } else {
            movieCurrentPosition = getArguments().getLong(CURRENT_POSITION, 0);
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextStep();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousStep();
            }
        });

        description.setText(currentStep.getDescription());

        if (!currentStep.getVideo().isEmpty()) {
            initializePlayer(Uri.parse(currentStep.getVideo()));
        } else if (!currentStep.getThumbnailUrl().isEmpty()) {
            initializePlayer(Uri.parse(currentStep.getThumbnailUrl()));
        } else {
            mPlayerView.setVisibility(View.INVISIBLE);
            image.setImageDrawable(cakeDrawable);
            image.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Nice cake image instead of video", Toast.LENGTH_SHORT).show();
        }

        initializeMediaSession();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);

        savedState.putLong(CURRENT_POSITION, movieCurrentPosition);
        savedState.putInt(CHOSEN_STEP_POSITION, position);

    }

    private void initializePlayer(Uri stepUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);


            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(stepUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(movieCurrentPosition);
            mExoPlayer.setPlayWhenReady(true);

            mExoPlayer.addListener(this);
            mPlayerView.findViewById(R.id.exo_fullscreen_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieCurrentPosition = mExoPlayer.getCurrentPosition();
                    Intent fullScreen = new Intent(getContext(), FullscreenActivity.class);
                    fullScreen.putExtra(CURRENT_POSITION, movieCurrentPosition);
                    fullScreen.putExtra(CHOSEN_STEP_POSITION, position);
                    fullScreen.putExtra(TWO_PANE, twoPane);
                    fullScreen.putExtra(SINGLE_RECIPE, singleRecipe);

                    if (!currentStep.getVideo().isEmpty()) {
                        fullScreen.putExtra(CURRENT_STEP, currentStep.getVideo());
                    } else {
                        fullScreen.putExtra(CURRENT_STEP, currentStep.getThumbnailUrl());
                    }
                    fullScreen.putParcelableArrayListExtra(STEPS, steps);
                    startActivity(fullScreen);
                }
            });
        }
    }

    private void nextStep() {
        if (position < steps.size() - 1) {
            position++;
            if (twoPane) {

                mCallback.onItemSelected(position);

            } else {

                Intent nextIntent = new Intent(getContext(), StepDetailedActivity.class);
                nextIntent.putExtra(CHOSEN_STEP_POSITION, position);
                nextIntent.putExtra(STEPS, steps);
                startActivity(nextIntent);
            }
        } else {
            Toast.makeText(getContext(), "There is no more steps - Cake is ready", Toast.LENGTH_SHORT).show();
        }

    }

    private void previousStep() {
        if (position <= steps.size() - 1 && position > 0) {
            position--;

            if(twoPane) {
                mCallback.onItemSelected(position);
            } else {
                Intent previousIntent = new Intent(getContext(), StepDetailedActivity.class);
                previousIntent.putExtra(CHOSEN_STEP_POSITION, position);
                previousIntent.putExtra(STEPS, steps);
                startActivity(previousIntent);
            }
        } else {
            Toast.makeText(getContext(), "Start with introduction", Toast.LENGTH_SHORT).show();
        }
    }

    private void releasePlayer() {
        if (null != mExoPlayer) {
            movieCurrentPosition = mExoPlayer.getCurrentPosition();
            if (twoPane) {
                playerCallback.setMovieCurrentPosition(movieCurrentPosition);
            }
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer(stepUri);
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    private void initializeMediaSession() {

        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new StepDetailedFragment.MySessionCallback());
        mMediaSession.setActive(true);
        mMediaSession.setActive(false);
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            playerCallback = (OnPlayerPause) context;
            mCallback = (RecipeStepsFragment.OnItemClickListener) context;

        } catch (ClassCastException e) {

        }
    }
}
