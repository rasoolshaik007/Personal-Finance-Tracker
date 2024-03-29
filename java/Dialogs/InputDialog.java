package edu.nwmissouri.personalfinancetracker.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import edu.nwmissouri.personalfinancetracker.R;

public class InputDialog {

    public interface OnInputListener {
        void onInput(String text);
    }

    public static void showInputDialog(Context context, String heading, String initialValue, int inputType, final OnInputListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.dialog_input, null);
        builder.setView(view);

        final EditText editText = view.findViewById(R.id.editTextNumber);
        editText.setInputType(inputType);
        editText.setText(initialValue);

        builder.setTitle(heading);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String input = editText.getText().toString();
            if (listener != null && !input.isEmpty() && !input.startsWith(" ")) {
                listener.onInput(input);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }
}
