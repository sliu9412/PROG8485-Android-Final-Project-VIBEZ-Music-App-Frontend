package com.example.androidfinal.dto.spotify;

import java.util.List;

public class SpotifyTrack {
    // Album associated with the track
    public SpotifyAlbum album;

    // List of artists associated with the track
    public List<SpotifyArtist> artists;

    // Disc number of the track
    public int disc_number;

    // Duration of the track in milliseconds
    public int duration_ms;

    // Whether the track is an episode
    public boolean episode;

    // Whether the track contains explicit content
    public boolean explicit;

    // API endpoint for the track
    public String href;

    // ID of the track
    public String id;

    // Whether the track is local to the user's device
    public boolean is_local;

    // Name of the track
    public String name;

    // Popularity of the track
    public int popularity;

    // URL for previewing the track
    public Object preview_url;

    // Whether the item is a track
    public boolean track;

    // Track number within the album
    public int track_number;

    // Type of the item
    public String type;

    // URI of the track
    public String uri;
}
