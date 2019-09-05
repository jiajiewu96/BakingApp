package com.example.bakingapp.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


import com.example.bakingapp.R;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;

import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import static com.example.bakingapp.utils.Consts.EXOPLAYER_KEY;
import static com.example.bakingapp.utils.Consts.FLAG_NEXT;
import static com.example.bakingapp.utils.Consts.FLAG_PREVIOUS;
import static com.example.bakingapp.utils.Consts.POSITION_KEY;
import static com.example.bakingapp.utils.Consts.STEP_KEY;

public class RecipeStepDetailFragment extends Fragment implements ExoPlayer.EventListener {

    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private long mPlayerPosition;
    private static final String TAG = RecipeStepDetailFragment.class.getSimpleName();

    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private ArrayList<Step> steps;
    private int position;
    private FragmentActivity mFragmentActivity;


    private Context sContext;
    private TextView mLongStepTextView;
    private ImageView mNextButton;
    private ImageView mPreviousButton;

    OnStepChangeClickListener mCallback;
    private FragmentActivity mActivity;
    private ActionBar mActionBar;
    private Uri mVideoUri;

    public interface OnStepChangeClickListener {
        void onStepChanged(int flag, int position, ArrayList<Step> steps);
    }


    public RecipeStepDetailFragment() {
        steps = new ArrayList<>();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (sContext == null) {
            sContext = context;
        }
        mActivity = getActivity();
        try {
            mCallback = (OnStepChangeClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepChangeClickListener");
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentActivity = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        mActionBar = ((AppCompatActivity) mFragmentActivity).getSupportActionBar();

        if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            mActionBar.hide();
        }else{
            mActionBar.show();
        }
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.player_view);
        mLongStepTextView = (TextView) rootView.findViewById(R.id.tv_step_long);
        mNextButton = (ImageView) rootView.findViewById(R.id.iv_next_step);
        mPreviousButton = (ImageView) rootView.findViewById(R.id.iv_previous_step);

        Bundle bundle = null;

        if (getArguments() != null) {
            bundle = getArguments();
            steps = bundle.getParcelableArrayList(STEP_KEY);
            position = bundle.getInt(POSITION_KEY);
        }


        setUpStepDetail();
        if(savedInstanceState != null){
            mPlayerPosition = savedInstanceState.getLong(EXOPLAYER_KEY);
            if(mPlayerPosition != C.TIME_UNSET && mExoPlayer !=null){
                mExoPlayer.seekTo(mPlayerPosition);
                mExoPlayer.setPlayWhenReady(true);
            }
        }
        setupStepNavigation();

        return rootView;
    }

    private void setupStepNavigation() {
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onStepChanged(FLAG_PREVIOUS, position, steps);
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onStepChanged(FLAG_NEXT, position, steps);
            }
        });
    }

    private void setUpStepDetail() {
        if (steps != null) {
            mLongStepTextView.setText(steps.get(position).getDescription());
            if (TextUtils.isEmpty(steps.get(position).getVideoURL())) {
                mPlayerView.setVisibility(View.GONE);
            } else if (isAdded() && sContext != null) {
                mPlayerView.setVisibility(View.VISIBLE);
                initializeMediaSession();
                mVideoUri = Uri.parse(steps.get(position).getVideoURL());
                initializePlayer(mVideoUri);

            }
        }
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(sContext, TAG);

        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );

        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE
                );
        mMediaSession.setPlaybackState(mStateBuilder.build());

        mMediaSession.setCallback(new RecipeSessionCallback());

        mMediaSession.setActive(true);

    }

    private void initializePlayer(Uri uri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(sContext, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(sContext,
                    Util.getUserAgent(sContext, "Baking App"));
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            mExoPlayer.prepare(mediaSource);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mExoPlayer!=null){
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            outState.putLong(EXOPLAYER_KEY, mPlayerPosition);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        mMediaSession.setActive(false);
        steps = null;
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_READY && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    private class RecipeSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }
}
