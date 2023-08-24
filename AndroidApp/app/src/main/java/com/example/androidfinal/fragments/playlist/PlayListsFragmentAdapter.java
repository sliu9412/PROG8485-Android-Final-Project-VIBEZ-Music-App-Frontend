package com.example.androidfinal.fragments.playlist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfinal.R;
import com.example.androidfinal.databinding.ActivityConatinerBinding;
import com.example.androidfinal.databinding.ListItemBinding;
import com.example.androidfinal.dto.spotify.PlayListItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlayListsFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Member variables
    public FragmentManager fragmentManager;
    public ActivityConatinerBinding activityConatinerBinding;
    List<PlayListItem> playlists;
    private ListItemBinding listItemBinding;

    // Constructor to pass required data to the adapter
    public PlayListsFragmentAdapter(List<PlayListItem> playlists, FragmentManager fragmentManager, ActivityConatinerBinding activityConatinerBinding) {
        super();
        this.playlists = playlists;
        this.fragmentManager = fragmentManager;
        this.activityConatinerBinding = activityConatinerBinding;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        listItemBinding = ListItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(listItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bindValue(playlists.get(position));
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemBinding listItemBinding;

        public ViewHolder(ListItemBinding listItemBinding) {
            super(listItemBinding.getRoot());
            this.listItemBinding = listItemBinding;
        }

        // Bind data to the view
        private void bindValue(PlayListItem playListItem) {
            listItemBinding.listItemTitle.setText(playListItem.name);
            listItemBinding.listItemSubtitle.setText(playListItem.trackCount + " pieces of music");
            if (playListItem.imageURL != null && !playListItem.imageURL.isEmpty()) {
                Picasso.get().load(playListItem.imageURL).into(listItemBinding.listItemCover);
            }
            
            // Handle click event
            listItemBinding.listItem.setOnClickListener(v -> {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new PlayListDetailFragment(activityConatinerBinding, playListItem));
                fragmentTransaction.commit();
            });
        }
    }
}
