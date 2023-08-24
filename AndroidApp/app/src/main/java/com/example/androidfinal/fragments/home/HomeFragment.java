package com.example.androidfinal.fragments.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.androidfinal.Settings;
import com.example.androidfinal.dto.api.HomeItem;
import com.example.androidfinal.models.IModel;
import com.example.androidfinal.utils.HttpRequest;
import com.example.androidfinal.utils.PreferencesHelper;
import com.example.androidfinal.databinding.FragmentHomeBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    FragmentHomeBinding fragmentHomeBinding;
    List<HomeItem> homeItems;

    // Retrieve home items from the server
    private void getHomeItems() {
        // Retrieve the Spotify token from shared preferences
        SharedPreferences preferences = PreferencesHelper.getPrivatePreferences(this.getActivity(), PreferencesHelper.PREF_SPOTIFY);
        String token = PreferencesHelper.get(preferences, PreferencesHelper.KEY_SPOTIFY_TOKEN, String.class);
        
        // Create an HTTP request using the Spotify token
        HttpRequest httpRequest = new HttpRequest(Settings.server_base_url, token);
        IModel service = httpRequest.buildTokenService();
        
        // Request home items from the server
        service.getHomeItems().enqueue(new Callback<List<HomeItem>>() {
            @Override
            public void onResponse(Call<List<HomeItem>> call, Response<List<HomeItem>> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                    homeItems = response.body();
                    setLayout();
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(getContext(), "Get Home Page Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HomeItem>> call, Throwable t) {
                // Handle network or request failure
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Set up the layout with retrieved home items
    private void setLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        fragmentHomeBinding.mainFragmentRecyclerview.setLayoutManager(linearLayoutManager);
        
        // Create an adapter and set it to the recycler view
        HomeFragmentAdapter homeFragmentAdapter = new HomeFragmentAdapter(homeItems);
        fragmentHomeBinding.mainFragmentRecyclerview.setAdapter(homeFragmentAdapter);
        homeFragmentAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout and initiate the process of getting home items
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        getHomeItems();
        return fragmentHomeBinding.getRoot();
    }
}
