package com.example.androidfinal.fragments.post;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.androidfinal.R;
import com.example.androidfinal.Settings;
import com.example.androidfinal.databinding.ActivityConatinerBinding;
import com.example.androidfinal.databinding.FragmentPostBinding;
import com.example.androidfinal.dto.api.Account;
import com.example.androidfinal.dto.api.HomeItem;
import com.example.androidfinal.dto.spotify.SpotifyArtist;
import com.example.androidfinal.dto.spotify.SpotifyImage;
import com.example.androidfinal.dto.spotify.SpotifyUser;
import com.example.androidfinal.dto.spotify.TrackRootItem;
import com.example.androidfinal.fragments.home.HomeFragment;
import com.example.androidfinal.models.IModel;
import com.example.androidfinal.utils.DialogHelper;
import com.example.androidfinal.utils.HttpRequest;
import com.example.androidfinal.utils.ImageHelper;
import com.example.androidfinal.utils.PreferencesHelper;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Add Post Fragment
 */
public class PostFragment extends Fragment implements View.OnClickListener {

    // Request code for image picker
    public static final int PICKER_REQUEST_CODE = 900;

    FragmentPostBinding postBinding;
    ActivityConatinerBinding activityConatinerBinding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    FragmentManager fragmentManager;
    Bitmap imagen;
    TrackRootItem trackItem;

    // Constructor to pass required data to the fragment
    public PostFragment(ActivityConatinerBinding activityConatinerBinding, TrackRootItem trackItem, FragmentManager fragmentManager) {
        this.activityConatinerBinding = activityConatinerBinding;
        this.trackItem = trackItem;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.postBinding = FragmentPostBinding.inflate(inflater, container, false);
        this.init();
        return postBinding.getRoot();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == this.postBinding.btnTakePhoto.getId()) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncher.launch(intent);
            } else {
                DialogHelper.showErrorDialog(getActivity(), "Permission camera does not granted");
            }
        } else if (v.getId() == this.postBinding.btnPublish.getId()) {
            if (validateForm()) {
                this.publishPost();
            }
        } else if (v.getId() == this.postBinding.btnChooseFromAlbum.getId()) {
            ImagePicker.with(getActivity())
                    .crop()
                    .compress(768)
                    .galleryOnly()
                    .maxResultSize(800, 800)
                    .createIntent(intent -> {
                        activityResultLauncher.launch(intent);
                        return null;
                    });
        }
    }

    private void init() {
        this.postBinding.btnTakePhoto.setOnClickListener(this);
        this.postBinding.btnChooseFromAlbum.setOnClickListener(this);
        this.postBinding.btnPublish.setOnClickListener(this);
        this.loadTrack();
        this.registerResultLauncher();
    }

    private void publishPost() {
        // ... Your existing code for publishing a post
    }

    private void loadTrack() {
        // ... Your existing code for loading track information
    }

    private void registerResultLauncher() {
        // ... Your existing code for registering result launcher
    }

    private void loadImage() {
        // ... Your existing code for loading and displaying image
    }

    private boolean validateForm() {
        // ... Your existing code for form validation
        return false;
    }

    private String getSongUrlImageFromTrack() {
        // ... Your existing code for getting song image URL from track
        return null;
    }

    private String getArtistFromTrack() {
        // ... Your existing code for getting artist name from track
        return null;
    }
}
