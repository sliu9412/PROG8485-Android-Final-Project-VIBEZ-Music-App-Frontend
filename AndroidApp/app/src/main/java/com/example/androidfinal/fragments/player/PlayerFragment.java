package com.example.androidfinal.fragments.player;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.androidfinal.Settings;
import com.example.androidfinal.utils.PreferencesHelper;
import com.example.androidfinal.databinding.FragmentPlayerBinding;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

public class PlayerFragment extends Fragment {
    String uri;
    FragmentPlayerBinding fragmentPlayerBinding;
    SpotifyAppRemote mSpotifyAppRemote;
    PlayerFragmentUiController playerFragmentUiController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentPlayerBinding = FragmentPlayerBinding.inflate(getLayoutInflater());
        getURI();
        // Initialize the UI controller
        playerFragmentUiController = new PlayerFragmentUiController(this);
        return fragmentPlayerBinding.getRoot();
    }

    // Retrieve the URI and play the track if provided
    private void getURI() {
        // Get the URI from preferences, if provided by another fragment, play the track
        SharedPreferences preferences = PreferencesHelper.getPrivatePreferences(getContext(), PreferencesHelper.PREF_SPOTIFY);
        uri = PreferencesHelper.get(preferences, PreferencesHelper.KEY_SPOTIFY_PLAYER_URI, String.class);
        
        // Delete the URI from preferences once retrieved
        if (uri != null) {
            PreferencesHelper.delete(preferences, PreferencesHelper.KEY_SPOTIFY_PLAYER_URI);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(Settings.spotify_client_id)
                        .setRedirectUri(Settings.spotify_redirect_uri)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(getContext(), connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        mSpotifyAppRemote.getPlayerApi()
                                .subscribeToPlayerState()
                                .setEventCallback(playerState -> {
                                    playerFragmentUiController.setTrackInfo();
                                });
                        if (uri != null) {
                            // Play the track if URI is provided
                            mSpotifyAppRemote.getPlayerApi().play(uri);
                        }
                    }

                    public void onFailure(Throwable throwable) {
                        // Handle Spotify connection failure
                        Toast.makeText(getContext(), "Connect Spotify Failed", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
