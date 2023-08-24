package com.example.androidfinal.models;

import com.example.androidfinal.dto.api.Account;
import com.example.androidfinal.dto.api.HomeItem;
import com.example.androidfinal.dto.spotify.PlayListsRoot;
import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Retrofit interface for making API requests.
 */
public interface IModel {
    // Get user information
    @GET("/apis/user")
    Call<JsonElement> getUserInfo();

    // Create a new user
    @POST("apis/user")
    Call<Account> createUser(
            @Body() Account account
    );

    // Log in a user
    @Headers("Content-Type: application/json")
    @POST("apis/user/login")
    Call<Account> login(@Body Account account);

    // Get playlists associated with a Spotify URI
    @GET("apis/playlists")
    Call<PlayListsRoot> getPlayLists(@Query("spotifyURI") String spotifyURI);

    // Get a list of home items
    @GET("apis/home")
    Call<List<HomeItem>> getHomeItems();

    // Create a new post
    @POST("apis/home/publish")
    Call<Account> createPost(
            @Body() HomeItem postItem
    );
}
