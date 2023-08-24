package com.example.androidfinal.activities.container;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.androidfinal.R;
import com.example.androidfinal.databinding.ActivityConatinerBinding;
import com.example.androidfinal.fragments.home.HomeFragment;
import com.example.androidfinal.fragments.player.PlayerFragment;
import com.example.androidfinal.fragments.playlist.PlayListsFragment;
import com.google.android.material.navigation.NavigationBarView;

public class ContainerActivity extends AppCompatActivity {

    ActivityConatinerBinding containerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        containerBinding = ActivityConatinerBinding.inflate(getLayoutInflater());
        setContentView(containerBinding.getRoot());

        // Initialize the container activity
        init();
    }

    // Initialize the container activity
    private void init() {
        // Disable the active item indicator in the bottom navigation bar
        containerBinding.bottomNavbar.setItemActiveIndicatorEnabled(false);

        // Set listener for bottom navigation bar item selection
        containerBinding.bottomNavbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle fragment replacement based on selected item
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if (item.getItemId() == R.id.player_nav_bar) {
                    fragmentTransaction.replace(R.id.frame, new PlayerFragment());
                } else if (item.getItemId() == R.id.playlist_nav_bar) {
                    fragmentTransaction.replace(R.id.frame, new PlayListsFragment(containerBinding));
                } else if (item.getItemId() == R.id.home_nav_bar) {
                    fragmentTransaction.replace(R.id.frame, new HomeFragment());
                }
                fragmentTransaction.commit();
                return true;
            }
        });

        // Set the default fragment to be displayed
        setDefaultFragment();
    }

    // Set the default fragment when the activity is launched
    private void setDefaultFragment() {
        // Set the default fragment to the home fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, new HomeFragment());
        fragmentTransaction.commit();
    }
}
