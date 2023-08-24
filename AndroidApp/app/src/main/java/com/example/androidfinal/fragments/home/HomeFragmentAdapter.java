package com.example.androidfinal.fragments.home;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfinal.Settings;
import com.example.androidfinal.databinding.HomeFragmentItemBinding;
import com.example.androidfinal.dto.api.HomeItem;
import com.example.androidfinal.utils.ImageHelper;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    HomeFragmentItemBinding homeFragmentItemBinding;
    List<HomeItem> homeItems;
    SpotifyAppRemote mSpotifyAppRemote;

    public HomeFragmentAdapter(List<HomeItem> homeItems) {
        this.homeItems = homeItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        homeFragmentItemBinding = HomeFragmentItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(homeFragmentItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bindView(homeItems.get(position));
    }

    @Override
    public int getItemCount() {
        return homeItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        HomeFragmentItemBinding homeFragmentItemBinding;

        public ViewHolder(HomeFragmentItemBinding homeFragmentItemBinding) {
            super(homeFragmentItemBinding.getRoot());
            this.homeFragmentItemBinding = homeFragmentItemBinding;
        }

        // Function to play the specified track using Spotify App Remote
        public void play(HomeItem homeItem) {
            if (mSpotifyAppRemote == null) {
                ConnectionParams connectionParams =
                        new ConnectionParams.Builder(Settings.spotify_client_id)
                                .setRedirectUri(Settings.spotify_redirect_uri)
                                .showAuthView(true)
                                .build();

                SpotifyAppRemote.connect(itemView.getContext(), connectionParams,
                        new Connector.ConnectionListener() {

                            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                                mSpotifyAppRemote = spotifyAppRemote;
                                if (homeItem.trackURI != null) {
                                    mSpotifyAppRemote.getPlayerApi().play(homeItem.trackURI);
                                }
                            }

                            public void onFailure(Throwable throwable) {
                                // Handle Spotify connection failure
                                Toast.makeText(itemView.getContext(), "Connect Spotify Failed", Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                if (homeItem.trackURI != null) {
                    mSpotifyAppRemote.getPlayerApi().play(homeItem.trackURI);
                }
            }
        }

        // Bind data from HomeItem to the view
        public void bindView(HomeItem homeItem) {
            Bitmap bitImage = ImageHelper.decodeImage(homeItem.image);
            homeFragmentItemBinding.homeFragmentUsername.setText(homeItem.username);
            homeFragmentItemBinding.homeFragmentPostTime.setText(homeItem.postTime);
            homeFragmentItemBinding.homeFragmentText.setText(homeItem.text);
            homeFragmentItemBinding.homeFragmentMusicArtist.setText(homeItem.trackArtist);
            homeFragmentItemBinding.homeFragmentMusicTitle.setText(homeItem.trackName);
            Picasso.get().load(homeItem.userImage).into(homeFragmentItemBinding.homeFragmentAvatar);
            homeFragmentItemBinding.homeFragmentPhoto.setImageBitmap(bitImage);
            Picasso.get().load(homeItem.trackImage).into(homeFragmentItemBinding.homeFragmentMusicCover);
            
            // Set click listener for the music button
            homeFragmentItemBinding.homeFragmentMusicBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    play(homeItem);
                }
            });
        }
    }
}
