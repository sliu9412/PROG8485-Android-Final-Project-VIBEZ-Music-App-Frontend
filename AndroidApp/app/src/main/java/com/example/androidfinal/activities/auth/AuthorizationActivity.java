package com.example.androidfinal.activities.auth;

import static com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfinal.Settings;
import com.example.androidfinal.activities.container.ContainerActivity;
import com.example.androidfinal.activities.login.LoginActivity;
import com.example.androidfinal.activities.register.RegisterActivity;
import com.example.androidfinal.databinding.ActivityAuthorizationBinding;
import com.example.androidfinal.dto.spotify.SpotifyUser;
import com.example.androidfinal.models.IModel;
import com.example.androidfinal.utils.HttpRequest;
import com.example.androidfinal.utils.JsonHelper;
import com.example.androidfinal.utils.PreferencesHelper;
import com.google.gson.JsonElement;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorizationActivity extends AppCompatActivity {

    private static final String TAG = AuthorizationActivity.class.getSimpleName();

    ActivityAuthorizationBinding authorizationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authorizationBinding = ActivityAuthorizationBinding.inflate(getLayoutInflater());
        setContentView(authorizationBinding.getRoot());

        // Initialize UI elements and listeners
        init();

        // Start the Spotify authorization process
        startAuthorization();
    }

    // Initialize UI elements and listeners
    private void init() {
        authorizationBinding.authorizationFailBtn.setOnClickListener(v -> {
            // Redirect to the login activity if authorization fails
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    // Start the process to authorize the Spotify App
    private void startAuthorization() {
        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(Settings.spotify_client_id, AuthorizationResponse.Type.TOKEN, Settings.spotify_redirect_uri);

        // Set required scopes for Spotify authorization
        builder.setScopes(new String[]{"streaming", "user-read-email", "user-read-private"});
        AuthorizationRequest request = builder.build();

        // Open the Spotify login activity to initiate authorization
        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    // Store the Spotify access token in SharedPreferences
    private void setSpotifyToken(String accessToken) {
        Log.i(TAG, "Spotify access token: " + accessToken);
        SharedPreferences preferences = PreferencesHelper.getPrivatePreferences(this, PreferencesHelper.PREF_SPOTIFY);
        PreferencesHelper.put(preferences, PreferencesHelper.KEY_SPOTIFY_TOKEN, accessToken);
    }

    // Store Spotify user information in SharedPreferences
    private void setUserInfo(SpotifyUser spotifyUser) {
        SharedPreferences preferences = PreferencesHelper.getPrivatePreferences(this, PreferencesHelper.PREF_SPOTIFY);
        PreferencesHelper.put(preferences, PreferencesHelper.KEY_SPOTIFY_USER, spotifyUser);
    }

    // Redirect to appropriate activity based on user's state
    private void redirectActivity(SpotifyUser spotifyUser) {
        SharedPreferences preferences = PreferencesHelper.getPrivatePreferences(this, PreferencesHelper.PREF_SPOTIFY);
        final String redirectValue = PreferencesHelper.get(preferences, PreferencesHelper.KEY_REDIRECT_ACTIVITY, String.class);
        Intent intent2Main = null;


        // Determine the next activity based on the redirection value
        if (redirectValue.equalsIgnoreCase(RegisterActivity.class.getSimpleName())) {
            if (spotifyUser.actualUserID == null) {
                intent2Main = new Intent(AuthorizationActivity.this, RegisterActivity.class);
            } else {
                // User already has an account, redirect to login with a message
                intent2Main = new Intent(AuthorizationActivity.this, LoginActivity.class);
                intent2Main.putExtra(LoginActivity.INTENT_PARAM_DIALOG_MESSAGE,
                        "You already have an account with your email '"
                                + spotifyUser.email + "', please login");
            }
        } else if (redirectValue.equalsIgnoreCase(LoginActivity.class.getSimpleName())) {
            intent2Main = new Intent(AuthorizationActivity.this, LoginActivity.class);
        } else if (redirectValue.equalsIgnoreCase(ContainerActivity.class.getSimpleName())) {
            intent2Main = new Intent(AuthorizationActivity.this, ContainerActivity.class);
        }

        // Start the appropriate activity
        startActivity(intent2Main);
    }

    // Get user information from the server using the access token
    private void getUserInfo(String token) {
        HttpRequest httpRequest = new HttpRequest(Settings.server_base_url, token);
        IModel service = httpRequest.buildService(IModel.class, true);
        service.getUserInfo().enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Parse the response and update user information
                    String jsonBody = response.body().getAsJsonObject().get("message").toString();
                    final SpotifyUser spotifyUser = JsonHelper.parse(jsonBody, SpotifyUser.class);
                    if (spotifyUser != null) {
                        try {
                            // Update additional user attributes if available
                            final String actualUserID = JsonHelper.getStrFromJsonElement(
                                    response.body().getAsJsonObject().get("actualID"));
                            if (actualUserID != null) {
                                spotifyUser.actualUserID = actualUserID;
                            }
                            final String username = JsonHelper.getStrFromJsonElement(
                                    response.body().getAsJsonObject().get("username"));
                            if (username != null) {
                                spotifyUser.username = username;
                            }
                        } catch (Exception err) {
                            Log.e(TAG, "User without actualUserID attribute");
                        }

                        // Store user info and redirect to appropriate activity
                        setUserInfo(spotifyUser);
                        redirectActivity(spotifyUser);
                    } else {
                        // Display error message from the response
                        String errorMessage = response.body()
                                .getAsJsonObject().get("error")
                                .getAsJsonObject().get("message").getAsString();
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Display failure message if unable to get user info
                    Toast.makeText(getApplicationContext(), "Get User Info Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                // Display error message in case of network failure
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if the result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    setSpotifyToken(response.getAccessToken());
                    getUserInfo(response.getAccessToken());
                    // Handle successful response
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Toast.makeText(getApplicationContext(), "Spotify Authorize User Failed", Toast.LENGTH_LONG).show();
                    break;
                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }
}
