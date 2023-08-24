package com.example.androidfinal.fragments.player;

public class TimeConverter {
    // Convert milliseconds to seconds
    public static int toSecond(long millisecond) {
        return (int) Math.ceil(millisecond / 1000);
    }

    // Convert milliseconds to minute:second format
    public static String convert(long millisecond) {
        long longTypeMinute = millisecond / 1000 / 60;
        int minute = (int) longTypeMinute;
        int remnantSecond = (int) ((millisecond - (minute * 1000 * 60)) / 1000) + 1;

        // Format the remnant seconds with leading zero if less than 10
        if (remnantSecond < 10) {
            return minute + ":0" + remnantSecond;
        } else {
            return minute + ":" + remnantSecond;
        }
    }
}
