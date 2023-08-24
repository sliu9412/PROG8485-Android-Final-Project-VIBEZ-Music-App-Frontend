package com.example.androidfinal.dto.api;

public class HomeItem {
    // User information
    public String userID;
    public String username;
    public String userImage;

    // Track information
    public String trackURI;
    public String trackName;
    public String trackArtist;
    public String trackImage;

    // Item image and text
    public String image;
    public String text;

    // Time when the post was made
    public String postTime;

    // Override toString method for debugging
    @Override
    public String toString() {
        return "HomeItem{" +
                "userID='" + userID + '\'' +
                ", username='" + username + '\'' +
                ", userImage='" + userImage + '\'' +
                ", trackURI='" + trackURI + '\'' +
                ", trackName='" + trackName + '\'' +
                ", trackArtist='" + trackArtist + '\'' +
                ", trackImage='" + trackImage + '\'' +
                ", image='" + image + '\'' +
                ", text='" + text + '\'' +
                ", postTime='" + postTime + '\'' +
                '}';
    }
}
