package com.example.androidfinal.fragments.playlist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfinal.databinding.ActivityConatinerBinding;
import com.example.androidfinal.databinding.ListItemBinding;
import com.example.androidfinal.dto.spotify.SpotifyImage;
import com.example.androidfinal.dto.spotify.TrackRootItem;
import com.example.androidfinal.fragments.post.PostFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlayListDetailFragmentAdapter extends RecyclerView.Adapter<PlayListDetailFragmentAdapter.ViewHolder> {

    ListItemBinding listItemBinding;
    FragmentManager fragmentManager;
    private List<TrackRootItem> tracksList;
    private ActivityConatinerBinding activityConatinerBinding;

    public PlayListDetailFragmentAdapter(List<TrackRootItem> tracksList, FragmentManager fragmentManager, ActivityConatinerBinding activityConatinerBinding) {
        super();
        this.tracksList = tracksList;
        this.fragmentManager = fragmentManager;
        this.activityConatinerBinding = activityConatinerBinding;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        listItemBinding = ListItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(listItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindValue(tracksList.get(position));
    }

    @Override
    public int getItemCount() {
        return tracksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ListItemBinding listItemBinding;

        public ViewHolder(ListItemBinding listItemBinding) {
            super(listItemBinding.getRoot());
            this.listItemBinding = listItemBinding;
        }

        // Bind the data to the ViewHolder
        private void bindValue(TrackRootItem trackItem) {
            SpotifyImage image = trackItem.track.album.images.stream()
                    .filter(img -> img.height == 64)
                    .findAny()
                    .orElse(null);
            listItemBinding.listItemTitle.setText(trackItem.track.name);
            listItemBinding.listItemSubtitle.setText(trackItem.track.artists.get(0).name);
            
            // Load image using Picasso if available
            if (image != null) {
                Picasso.get().load(image.url).into(listItemBinding.listItemCover);
            }
            
            // Set click event for opening the PostFragment
            listItemBinding.listItem.setOnClickListener(v -> {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(com.example.androidfinal.R.id.frame, new PostFragment(activityConatinerBinding, trackItem, fragmentManager));
                fragmentTransaction.commit();
            });
        }

    }

}
