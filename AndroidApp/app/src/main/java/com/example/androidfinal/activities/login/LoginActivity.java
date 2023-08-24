package com.example.androidfinal.activities.login;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.androidfinal.Settings;
import com.example.androidfinal.activities.auth.AuthorizationActivity;
import com.example.androidfinal.activities.container.ContainerActivity;
import com.example.androidfinal.activities.register.RegisterActivity;
import com.example.androidfinal.databinding.ActivityLoginBinding;
import com.example.androidfinal.dto.api.Account;
import com.example.androidfinal.dto.spotify.SpotifyUser;
import com.example.androidfinal.models.IModel;
import com.example.androidfinal.utils.DialogHelper;
import com.example.androidfinal.utils.HttpRequest;
import com.example.androidfinal.utils.PreferencesHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final int CAMERA_PERMISSION_CODE = 1;
    public static final String INTENT_PARAM_DIALOG_MESSAGE = "INFO_MESSAGE";

    private ActivityLoginBinding activityLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());

        // Initialize the login activity
        init();
    }

    // Initialize the login activity
    private void init() {
        // Set click listeners for login and register buttons
        activityLoginBinding.accountRegisterLoginBtn.setOnClickListener(this);
        activityLoginBinding.accountRegisterRegisterBtn.setOnClickListener(this);

        // Display dialog message if available from previous activity
        final String dialogMessage = this.getIntent().getStringExtra(INTENT_PARAM_DIALOG_MESSAGE);
        if (dialogMessage != null) {
            DialogHelper.showInfoDialog(this, dialogMessage);
        }

        // Check and request camera permission
        checkCameraPermission();
    }

    @Override
    public void onClick(View v) {
        SharedPreferences preferences = PreferencesHelper.getPrivatePreferences(this, PreferencesHelper.PREF_SPOTIFY);
        if (v.getId() == activityLoginBinding.accountRegisterLoginBtn.getId()) {
            SpotifyUser spotifyUser = PreferencesHelper.get(preferences, PreferencesHelper.KEY_SPOTIFY_USER, SpotifyUser.class);
            if (spotifyUser == null) {
                PreferencesHelper.put(preferences, PreferencesHelper.KEY_REDIRECT_ACTIVITY, LoginActivity.class.getSimpleName());
                Intent intent = new Intent(this, AuthorizationActivity.class);
                startActivity(intent);
            } else if ((new LoginActivityValidation(activityLoginBinding)).validate()) {
                Account account = new Account();
                account.email = activityLoginBinding.accountRegisterEmail.getText().toString();
                account.password = activityLoginBinding.accountRegisterPassword.getText().toString();

                // Perform login API call
                HttpRequest httpRequest = new HttpRequest(Settings.server_base_url, null);
                IModel service = httpRequest.buildService();
                service.login(account).enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        if (response.isSuccessful()) {
                            if (response.body().message == null) {
                                // Set redirect activity and update user info
                                PreferencesHelper.put(preferences, PreferencesHelper.KEY_REDIRECT_ACTIVITY, ContainerActivity.class.getSimpleName());
                                SpotifyUser spotifyUser = PreferencesHelper.get(preferences, PreferencesHelper.KEY_SPOTIFY_USER, SpotifyUser.class);
                                spotifyUser.actualUserID = response.body().userID;
                                spotifyUser.username = response.body().username;
                                PreferencesHelper.put(preferences, PreferencesHelper.KEY_SPOTIFY_USER, spotifyUser);
                                Intent intent = new Intent(LoginActivity.this, AuthorizationActivity.class);
                                startActivity(intent);
                            } else {
                                // Display error message from response
                                Toast.makeText(getApplicationContext(), response.body().message, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        // Display login failure message
                        Toast.makeText(getApplicationContext(), "Login Failed, " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else if (v.getId() == activityLoginBinding.accountRegisterRegisterBtn.getId()) {
            // Set redirect activity for registration and start authorization
            PreferencesHelper.put(preferences, PreferencesHelper.KEY_REDIRECT_ACTIVITY, RegisterActivity.class.getSimpleName());
            Intent intent = new Intent(this, AuthorizationActivity.class);
            startActivity(intent);
        }
    }

    // Check and request camera permission if needed
    private void checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    // Handle camera permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Camera permission granted");
            }
        }
    }
}
