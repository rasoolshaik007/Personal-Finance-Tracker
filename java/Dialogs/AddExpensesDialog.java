package edu.nwmissouri.personalfinancetracker.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.nwmissouri.personalfinancetracker.R;
import edu.nwmissouri.personalfinancetracker.data.Expenses;
import edu.nwmissouri.personalfinancetracker.helper.SharedPreferencesUtils;

public class AddExpensesDialog {

    public interface OnCompleteListener {
        void onDone(Expenses expenses);
    }

    public static void showDialog(Context context, final OnCompleteListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        Calendar selectedDateTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String[] expensesTypes = {
                "Grocery",
                "Dining Out",
                "Transportation",
                "Utilities",
                "Entertainment",
                "Healthcare",
                "Shopping",
                "Education",
                "Travel",
                "Insurance",
                "Home",
                "Gifts",
                "Miscellaneous"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, expensesTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        View view = inflater.inflate(R.layout.dialog_add_expenses, null);
        builder.setView(view);

        final EditText etAmount = view.findViewById(R.id.etAmount);
        final TextView tvDate = view.findViewById(R.id.tvDate);
        Spinner spinner = view.findViewById(R.id.spinnerType);
        spinner.setAdapter(adapter);
        final String[] selectedCategory = new String[1];
        selectedCategory[0] = expensesTypes[0];

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategory[0] = expensesTypes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        tvDate.setText(dateFormat.format(selectedDateTime.getTime()));

        builder.setTitle("Add Expenses");

        builder.setPositiveButton("Add", (dialog, which) -> {
            String amount = etAmount.getText().toString();

            if (listener != null && !amount.isEmpty() && !amount.startsWith(" ")) {
                Expenses ex = new Expenses(SharedPreferencesUtils.getUser(context), selectedCategory[0], Long.parseLong(amount), selectedDateTime.getTime());
                listener.onDone(ex);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        TimePickerDialog.OnTimeSetListener timeSetListener = (view12, hourOfDay, minute) -> {
            selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selectedDateTime.set(Calendar.MINUTE, minute);
            tvDate.setText(dateFormat.format(selectedDateTime.getTime()));
        };

        DatePickerDialog.OnDateSetListener dateSetListener = (view1, year, month, dayOfMonth) -> {
            selectedDateTime.set(Calendar.YEAR, year);
            selectedDateTime.set(Calendar.MONTH, month);
            selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    context,
                    timeSetListener,
                    selectedDateTime.get(Calendar.HOUR_OF_DAY),
                    selectedDateTime.get(Calendar.MINUTE),
                    true);

            timePickerDialog.show();
        };

        tvDate.setOnClickListener(view13 -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    dateSetListener,
                    selectedDateTime.get(Calendar.YEAR),
                    selectedDateTime.get(Calendar.MONTH),
                    selectedDateTime.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.show();
        });

        builder.create().show();
    }


}
