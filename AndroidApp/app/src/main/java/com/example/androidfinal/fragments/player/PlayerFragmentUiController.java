package com.example.androidfinal.fragments.player;

import android.os.Handler;
import android.os.Looper;
import android.widget.SeekBar;

import androidx.core.content.ContextCompat;

import com.example.androidfinal.R;
import com.example.androidfinal.databinding.FragmentPlayerBinding;

public class PlayerFragmentUiController {

    PlayerFragment playerFragment;
    FragmentPlayerBinding fragmentPlayerBinding;
    private Handler timeLoopHandler;

    public PlayerFragmentUiController(PlayerFragment playerFragment) {
        this.playerFragment = playerFragment;
        fragmentPlayerBinding = playerFragment.fragmentPlayerBinding;
        setEventListeners();
    }

    // Set the music name, artists, and related info
    public void setTrackInfo() {
        playerFragment.mSpotifyAppRemote.getPlayerApi().getPlayerState().setResultCallback(playerState -> {
            // Set cover of the track
            playerFragment.mSpotifyAppRemote.getImagesApi().getImage(playerState.track.imageUri).setResultCallback(bitmap -> {
                fragmentPlayerBinding.playerMusicCover.setImageBitmap(bitmap);
            });
            // Set music title
            fragmentPlayerBinding.playerMusicTitle.setText(playerState.track.name);
            // Set artist
            fragmentPlayerBinding.playerArtistName.setText(playerState.track.artist.name);
            // Set duration
            fragmentPlayerBinding.playerEndTime.setText(TimeConverter.convert(playerState.track.duration));
            // Set button status based on pause state
            if (playerState.isPaused) {
                fragmentPlayerBinding.playerPlayBtn.setBackground(ContextCompat.getDrawable(playerFragment.getActivity(), R.drawable.player_play_btn));
            } else {
                fragmentPlayerBinding.playerPlayBtn.setBackground(ContextCompat.getDrawable(playerFragment.getActivity(), R.drawable.player_pause_btn));
            }
        });
        startUpdatingPlayingTime();
    }

    // Start updating the playing time every second
    public void startUpdatingPlayingTime() {
        timeLoopHandler = new Handler(Looper.getMainLooper());
        timeLoopHandler.post(new Runnable() {
            @Override
            public void run() {
                playerFragment.mSpotifyAppRemote.getPlayerApi().getPlayerState().setResultCallback(playerState -> {
                    fragmentPlayerBinding.playerCurrentTime.setText(TimeConverter.convert(playerState.playbackPosition));
                    fragmentPlayerBinding.playerMusicSeekbar.setMax(TimeConverter.toSecond(playerState.track.duration));
                    fragmentPlayerBinding.playerMusicSeekbar.setProgress(TimeConverter.toSecond(playerState.playbackPosition));
                });
                timeLoopHandler.postDelayed(this, 1000);
            }
        });
    }

    // Set event listeners for UI controls
    private void setEventListeners() {
        // Play button play and pause event
        fragmentPlayerBinding.playerPlayBtn.setOnClickListener(v -> {
            playerFragment.mSpotifyAppRemote.getPlayerApi().getPlayerState().setResultCallback(playerState -> {
                if (playerState.isPaused) {
                    playerFragment.mSpotifyAppRemote.getPlayerApi().resume();
                } else {
                    playerFragment.mSpotifyAppRemote.getPlayerApi().pause();
                }
            });
        });

        // Drag seekbar event
        fragmentPlayerBinding.playerMusicSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    playerFragment.mSpotifyAppRemote.getPlayerApi().seekTo((long) progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // No action needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // No action needed
            }
        });

        // Click next song button
        fragmentPlayerBinding.playerNextBtn.setOnClickListener(v -> {
            playerFragment.mSpotifyAppRemote.getPlayerApi().skipNext();
        });

        // Click previous song button
        fragmentPlayerBinding.playerPrevBtn.setOnClickListener(v -> {
            playerFragment.mSpotifyAppRemote.getPlayerApi().skipPrevious();
        });
    }
}
