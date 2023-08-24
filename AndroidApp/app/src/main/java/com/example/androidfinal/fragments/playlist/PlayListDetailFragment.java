package com.example.androidfinal.fragments.playlist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.androidfinal.Settings;
import com.example.androidfinal.databinding.ActivityConatinerBinding;
import com.example.androidfinal.databinding.FragmentPlayListDetailBinding;
import com.example.androidfinal.dto.spotify.PlayListItem;
import com.example.androidfinal.dto.spotify.TrackRootItem;
import com.example.androidfinal.models.ISpotify;
import com.example.androidfinal.utils.HttpRequest;
import com.example.androidfinal.utils.JsonHelper;
import com.example.androidfinal.utils.PreferencesHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * PlayList Detail Fragment.
 */
public class PlayListDetailFragment extends Fragment {

    ActivityConatinerBinding activityConatinerBinding;
    PlayListItem playlist;
    FragmentPlayListDetailBinding playListDetailBinding;
    List<TrackRootItem> tracksResponseList;

    // Constructor to pass the required data to the fragment
    public PlayListDetailFragment(ActivityConatinerBinding activityConatinerBinding, PlayListItem playlist) {
        this.activityConatinerBinding = activityConatinerBinding;
        this.playlist = playlist;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.playListDetailBinding = FragmentPlayListDetailBinding.inflate(inflater, container, false);
        this.init();
        return playListDetailBinding.getRoot();
    }

    // Initialize the fragment
    private void init() {
        // Set the title of the playlist on the top bar
        this.playListDetailBinding.playlistTopBar.topNavbarTitle.setText(this.playlist.name);
        
        // Retrieve the Spotify token from SharedPreferences
        SharedPreferences preferences = PreferencesHelper.getPrivatePreferences(this.getActivity(), PreferencesHelper.PREF_SPOTIFY);
        String token = PreferencesHelper.get(preferences, PreferencesHelper.KEY_SPOTIFY_TOKEN, String.class);
        
        // Create an HTTP request using the Spotify token
        HttpRequest httpRequest = new HttpRequest(Settings.spotify_api_url, token);
        ISpotify service = httpRequest.buildService(ISpotify.class, true);
        
        // Request playlist tracks from the server
        service.playlistTracks(getPlayListIdByUri(this.playlist)).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonArray itemsArray = response.body().getAsJsonObject().get("items").getAsJsonArray();
                    if (itemsArray.isJsonArray()) {
                        String jsonTracks = itemsArray.toString();
                        tracksResponseList = JsonHelper.GSON.fromJson(jsonTracks, new TypeToken<List<TrackRootItem>>() {
                        }.getType());
                        setLayout();
                    }
                } else {
                    Toast.makeText(getContext(), "Get PlayLists Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Set the layout with retrieved playlist tracks
    private void setLayout() {
        // Set title on the top bar with the track count
        playListDetailBinding.playlistTopBar.topNavbarTitle.setText(playlist.name + "(" + tracksResponseList.size() + ")");
        
        // Set up the RecyclerView for tracks
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        playListDetailBinding.playistTracksRv.setLayoutManager(linearLayoutManager);
        PlayListDetailFragmentAdapter playListsFragmentAdapter = new PlayListDetailFragmentAdapter(tracksResponseList, getActivity().getSupportFragmentManager(), activityConatinerBinding);
        playListDetailBinding.playistTracksRv.setAdapter(playListsFragmentAdapter);
        playListsFragmentAdapter.notifyDataSetChanged();
    }

    // Get the playlist ID from the URI
    private String getPlayListIdByUri(PlayListItem playlist) {
        String[] uriSplit = playlist.uri.split(":");
        return uriSplit[uriSplit.length - 1];
    }
}
