package com.example.androidfinal.fragments.playlist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.androidfinal.R;
import com.example.androidfinal.Settings;
import com.example.androidfinal.databinding.ActivityConatinerBinding;
import com.example.androidfinal.databinding.FragmentPlayListsBinding;
import com.example.androidfinal.dto.spotify.PlayListItem;
import com.example.androidfinal.dto.spotify.PlayListsRoot;
import com.example.androidfinal.dto.spotify.SpotifyUser;
import com.example.androidfinal.models.IModel;
import com.example.androidfinal.utils.HttpRequest;
import com.example.androidfinal.utils.PreferencesHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayListsFragment extends Fragment {

    FragmentPlayListsBinding fragmentPlayListsBinding;
    List<PlayListItem> playlists;
    ActivityConatinerBinding containerActivity;

    // Constructor to pass the required data to the fragment
    public PlayListsFragment(ActivityConatinerBinding containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentPlayListsBinding = FragmentPlayListsBinding.inflate(inflater, container, false);
        this.init();
        return fragmentPlayListsBinding.getRoot();
    }

    // Initialize the fragment
    private void init() {
        getUserPlayLists();
    }

    // Set up the layout with the retrieved playlists
    private void setLayout() {
        // Set top bar title
        fragmentPlayListsBinding.playlistTopBar.topNavbarTitle.setText(R.string.player_top_navbar_title);
        SharedPreferences preferences = PreferencesHelper.getPrivatePreferences(this.getActivity(), PreferencesHelper.PREF_SPOTIFY);
        SpotifyUser spotifyUser = PreferencesHelper.get(preferences, PreferencesHelper.KEY_SPOTIFY_USER, SpotifyUser.class);
        
        // Set up the RecyclerView for playlists
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentPlayListsBinding.playlistRecycleView.setLayoutManager(linearLayoutManager);
        PlayListsFragmentAdapter playListsFragmentAdapter = new PlayListsFragmentAdapter(playlists, getActivity().getSupportFragmentManager(), containerActivity);
        fragmentPlayListsBinding.playlistRecycleView.setAdapter(playListsFragmentAdapter);
        playListsFragmentAdapter.notifyDataSetChanged();
    }

    // Retrieve user playlists from the server
    private void getUserPlayLists() {
        SharedPreferences preferences = PreferencesHelper.getPrivatePreferences(this.getActivity(), PreferencesHelper.PREF_SPOTIFY);
        String token = PreferencesHelper.get(preferences, PreferencesHelper.KEY_SPOTIFY_TOKEN, String.class);
        SpotifyUser spotifyUser = PreferencesHelper.get(preferences, PreferencesHelper.KEY_SPOTIFY_USER, SpotifyUser.class);
        HttpRequest httpRequest = new HttpRequest(Settings.server_base_url, token);
        IModel service = httpRequest.buildTokenService();
        service.getPlayLists(spotifyUser.uri).enqueue(new Callback<PlayListsRoot>() {
            @Override
            public void onResponse(Call<PlayListsRoot> call, Response<PlayListsRoot> response) {
                if (response.isSuccessful()) {
                    if (response.body().message != null) {
                        Toast.makeText(getContext(), response.body().message, Toast.LENGTH_SHORT).show();
                    } else {
                        playlists = response.body().data;
                        setLayout();
                    }
                } else {
                    Toast.makeText(getContext(), "Get PlayLists Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlayListsRoot> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
