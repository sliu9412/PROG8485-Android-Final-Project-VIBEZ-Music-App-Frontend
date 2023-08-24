package com.example.androidfinal.utils;

import android.content.Context;
import android.content.DialogInterface;

import com.example.androidfinal.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * Dialog Helper.
 */
public final class DialogHelper {

    /**
     * Create and show a generic info alert dialog.
     *
     * @param context
     * @param message
     */
    public static void showInfoDialog(Context context, String message) {
        showDialog(context, context.getString(R.string.dialog_info_title), message);
    }

    /**
     * Crete and show a generic error dialog.
     *
     * @param context
     * @param message
     */
    public static void showErrorDialog(Context context, String message) {
        showDialog(context, context.getString(R.string.dialog_error_title), message);
    }

    /**
     * Create and show a generic alert dialog with an specific title and message.
     *
     * @param context
     * @param title
     * @param message
     */
    public static void showDialog(Context context, String title, String message) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_ok_btn, (dialog, which) -> {
                })
                .setNegativeButton(R.string.dialog_cancel_btn, (dialog, which) -> {
                }).show();
    }

    /**
     * Create and show a generic alert dialog with an specific title, message, and
     * receive dialog on click listener to communicate the events.
     *
     * @param context
     * @param title
     * @param message
     * @param listener
     */
    public static void showDialog(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_ok_btn, listener)
                .setNegativeButton(R.string.dialog_cancel_btn, listener).show();
    }

    private DialogHelper() {
    }
}
