package com.example.androidfinal.dto.spotify;

import java.util.List;

public class SpotifyUser {
    // User's display name
    public String display_name;

    // API endpoint for the user
    public String href;

    // ID of the user
    public String id;

    // List of images associated with the user
    public List<SpotifyImage> images;

    // Type of the user
    public String type;

    // URI of the user
    public String uri;

    // User's country
    public String country;

    // User's product type
    public String product;

    // User's email address
    public String email;

    // Actual user ID
    public String actualUserID;

    // User's username
    public String username;

    // Override toString method for debugging
    @Override
    public String toString() {
        return "SpotifyUser{" +
                "display_name='" + display_name + '\'' +
                ", href='" + href + '\'' +
                ", id='" + id + '\'' +
                ", images=" + images +
                ", type='" + type + '\'' +
                ", uri='" + uri + '\'' +
                ", country='" + country + '\'' +
                ", product='" + product + '\'' +
                ", email='" + email + '\'' +
                ", actualUserID='" + actualUserID + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
