package com.example.androidfinal.dto.api;

public class Account {
    // API response message
    public String message;

    // User account information
    public String username;
    public String email;
    public String password;
    public String spotifyURI;
    public String profileImgMinURL;

    // User ID associated with the account
    public String userID;

    // Override toString method for debugging
    @Override
    public String toString() {
        return "Account{" +
                "message='" + message + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", spotifyURI='" + spotifyURI + '\'' +
                ", profileImgMinURL='" + profileImgMinURL + '\'' +
                ", userID='" + userID + '\'' +
                '}';
    }
}
