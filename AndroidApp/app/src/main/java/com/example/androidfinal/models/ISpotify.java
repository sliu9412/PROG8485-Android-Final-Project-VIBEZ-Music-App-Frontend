package com.example.androidfinal.models;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit interface for making Spotify API requests.
 */
public interface ISpotify {

    // Search for songs
    @GET("/v1/search")
    Call<JsonElement> searchSongs(
            @Query(value = "q", encoded = true) String q,
            @Query("type") String type
    );

    // Get tracks from a playlist
    @GET("/v1/playlists/{playlistId}/tracks")
    Call<JsonElement> playlistTracks(@Path("playlistId") String playlistID);

}
