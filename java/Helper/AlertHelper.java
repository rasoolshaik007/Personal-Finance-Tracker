package edu.nwmissouri.personalfinancetracker.helper;

import android.content.Context;
import android.app.AlertDialog;

public class AlertHelper {
    public static void showOkAlert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
