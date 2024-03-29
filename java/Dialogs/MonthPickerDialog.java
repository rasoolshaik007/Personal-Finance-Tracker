package edu.nwmissouri.personalfinancetracker.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import edu.nwmissouri.personalfinancetracker.R;

public class MonthPickerDialog extends DialogFragment {

    private OnMonthSetListener listener;

    public interface OnMonthSetListener {
        void onMonthSet(int year, int month);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = requireContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_month_picker, null);
        final NumberPicker monthPicker = view.findViewById(R.id.monthPicker);
        final NumberPicker yearPicker = view.findViewById(R.id.yearPicker);

        // Set up month picker
        final String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(months.length - 1);
        monthPicker.setDisplayedValues(months);

        // Set up year picker (adjust range as needed)
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        yearPicker.setMinValue(currentYear - 10);
        yearPicker.setMaxValue(currentYear + 10);

        // Set default values to current month and year
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        monthPicker.setValue(currentMonth);
        yearPicker.setValue(currentYear);

        builder.setView(view)
                .setTitle("Select Month and Year")
                .setPositiveButton("OK", (dialog, which) -> {
                    int selectedMonth = monthPicker.getValue() + 1; // Month is zero-based
                    int selectedYear = yearPicker.getValue();
                    if (listener != null) {
                        listener.onMonthSet(selectedYear, selectedMonth);
                    }
                })
                .setNegativeButton("Cancel", null);

        return builder.create();
    }

    public void setOnMonthSetListener(OnMonthSetListener listener) {
        this.listener = listener;
    }
}
