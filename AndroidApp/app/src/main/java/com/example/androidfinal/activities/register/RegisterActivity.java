package com.example.androidfinal.activities.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfinal.Settings;
import com.example.androidfinal.activities.login.LoginActivity;
import com.example.androidfinal.databinding.ActivityRegisterBinding;
import com.example.androidfinal.dto.api.Account;
import com.example.androidfinal.dto.spotify.SpotifyImage;
import com.example.androidfinal.dto.spotify.SpotifyUser;
import com.example.androidfinal.models.IModel;
import com.example.androidfinal.utils.HttpRequest;
import com.example.androidfinal.utils.PreferencesHelper;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityRegisterBinding activityRegisterBinding;

    private SpotifyUser spotifyUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRegisterBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(activityRegisterBinding.getRoot());

        // Initialize the registration activity
        init();
    }

    // Initialize the registration activity
    private void init() {
        // Set click listeners for login and register buttons
        activityRegisterBinding.accountRegisterLoginBtn.setOnClickListener(this);
        activityRegisterBinding.accountRegisterRegisterBtn.setOnClickListener(this);

        // Load Spotify user info if available
        SharedPreferences preferences = PreferencesHelper.getPrivatePreferences(this, PreferencesHelper.PREF_SPOTIFY);
        this.spotifyUser = PreferencesHelper.get(preferences, PreferencesHelper.KEY_SPOTIFY_USER, SpotifyUser.class);
        if (this.spotifyUser != null) {
            if (this.spotifyUser.email != null && !this.spotifyUser.email.trim().isEmpty()) {
                this.activityRegisterBinding.accountRegisterEmail.setEnabled(false);
                this.activityRegisterBinding.accountRegisterEmail.setText(this.spotifyUser.email);
            } else {
                this.activityRegisterBinding.accountRegisterEmail.setEnabled(true);
            }

            // Load and display user's profile image
            if (this.spotifyUser.images != null && !this.spotifyUser.images.isEmpty()) {
                SpotifyImage image = this.spotifyUser.images.stream()
                        .filter(img -> img.height > 64)
                        .findAny()
                        .orElse(null);
                if (image != null) {
                    Picasso.get().load(image.url).into(this.activityRegisterBinding.imgProfile);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == activityRegisterBinding.accountRegisterRegisterBtn.getId()) {
            if ((new RegisterActivityValidation(activityRegisterBinding)).validate()) {
                // Build necessary objects for registration
                SharedPreferences preferences = PreferencesHelper.getPrivatePreferences(this, PreferencesHelper.PREF_SPOTIFY);
                SpotifyUser spotifyUser = PreferencesHelper.get(preferences, PreferencesHelper.KEY_SPOTIFY_USER, SpotifyUser.class);

                // Perform registration API call
                HttpRequest httpRequest = new HttpRequest(Settings.server_base_url, null);
                IModel service = httpRequest.buildTokenService();
                final Account account = new Account();
                account.email = activityRegisterBinding.accountRegisterEmail.getText().toString();
                account.password = activityRegisterBinding.accountRegisterPassword.getText().toString();
                account.username = activityRegisterBinding.accountRegisterUsername.getText().toString();
                account.spotifyURI = spotifyUser.uri;
                SpotifyImage imageMin = this.spotifyUser.images.stream()
                        .filter(img -> img.height == 64)
                        .findAny()
                        .orElse(null);
                if (imageMin != null) {
                    account.profileImgMinURL = imageMin.url;
                }
                service.createUser(account).enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        if (response.isSuccessful()) {
                            // Display registration success or failure message
                            Toast.makeText(getApplicationContext(), response.body().message, Toast.LENGTH_LONG).show();
                            if (response.body().message.equals("Create user successfully")) {
                                // Redirect to login activity with success message
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.putExtra(LoginActivity.INTENT_PARAM_DIALOG_MESSAGE,
                                        "The account has been created successfully. Please login.");
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Register Failed", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        // Display registration failure message
                        Toast.makeText(getApplicationContext(), "Register Failed, " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
        if (v.getId() == activityRegisterBinding.accountRegisterLoginBtn.getId()) {
            // Redirect to login activity
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }
}
