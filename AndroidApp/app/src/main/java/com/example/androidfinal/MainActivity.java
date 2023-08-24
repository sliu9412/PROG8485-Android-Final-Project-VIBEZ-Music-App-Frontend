package com.example.androidfinal;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfinal.activities.auth.AuthorizationActivity;
import com.example.androidfinal.activities.container.ContainerActivity;
import com.example.androidfinal.activities.register.RegisterActivity;
import com.example.androidfinal.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        mainBinding.button.setOnClickListener(v -> {
            Intent intent = new Intent(this, AuthorizationActivity.class);
            startActivity(intent);
        });

        mainBinding.player.setOnClickListener(v -> {
            Intent intent = new Intent(this, ContainerActivity.class);
            startActivity(intent);
        });

        mainBinding.register.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        // mainBinding.token.setText(getSpotifyToken());
        // mainBinding.uri.setText(getUserUri());
    }

    /*
    private String getSpotifyToken() {
        SharedPreferences preferences = PreferencesHelper.getPrivatePreferences(this, PreferencesHelper.PREF_SPOTIFY);
        String token = PreferencesHelper.get(preferences, PreferencesHelper.KEY_SPOTIFY_TOKEN, String.class);
        // SharedPreferences spotifyTokenSharePreference = getSharedPreferences(Settings.spotify_token_sharePreference_key, MODE_PRIVATE);
        return token == null ? "" : token;
    }

    private String getUserUri() {
        SharedPreferences spotifyTokenSharePreference = getSharedPreferences(Settings.userInfo_sharePreference_key, MODE_PRIVATE);
        return spotifyTokenSharePreference.getString(Settings.userInfo_uri, "");
    }
     */

}