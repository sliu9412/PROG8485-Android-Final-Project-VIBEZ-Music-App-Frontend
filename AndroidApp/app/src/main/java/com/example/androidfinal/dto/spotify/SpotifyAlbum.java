package com.example.androidfinal.dto.spotify;

import java.util.List;

public class SpotifyAlbum {
    // Type of the album
    public String album_type;

    // List of artists associated with the album
    public List<SpotifyArtist> artists;

    // List of available markets where the album is available
    public List<Object> available_markets;

    // API endpoint for the album
    public String href;

    // ID of the album
    public String id;

    // List of images associated with the album
    public List<SpotifyImage> images;

    // Name of the album
    public String name;

    // Release date of the album
    public String release_date;

    // Precision of the release date
    public String release_date_precision;

    // Total number of tracks in the album
    public int total_tracks;

    // Type of the album
    public String type;

    // URI of the album
    public String uri;
}
