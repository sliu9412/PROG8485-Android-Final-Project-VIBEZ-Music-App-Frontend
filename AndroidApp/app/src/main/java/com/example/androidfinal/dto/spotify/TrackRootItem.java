package com.example.androidfinal.dto.spotify;

import java.util.Date;

public class TrackRootItem {
    // Date when the track was added
    public Date added_at;

    // Whether the track is local to the user's device
    public boolean is_local;

    // Primary color of the track (could be null)
    public Object primary_color;

    // Track information
    public SpotifyTrack track;
}
