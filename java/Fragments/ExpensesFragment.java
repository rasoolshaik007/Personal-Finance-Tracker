package edu.nwmissouri.personalfinancetracker.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import edu.nwmissouri.personalfinancetracker.R;
import edu.nwmissouri.personalfinancetracker.adapter.ExpenseAdapter;
import edu.nwmissouri.personalfinancetracker.data.AppDao;
import edu.nwmissouri.personalfinancetracker.data.AppDatabase;
import edu.nwmissouri.personalfinancetracker.data.Expenses;
import edu.nwmissouri.personalfinancetracker.dialogs.AddExpensesDialog;
import edu.nwmissouri.personalfinancetracker.dialogs.MonthPickerDialog;
import edu.nwmissouri.personalfinancetracker.helper.SharedPreferencesUtils;

public class ExpensesFragment extends Fragment {

    private ExpenseAdapter expenseAdapter;

    AppDatabase db;
    AppDao dao;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat formattedMonthName;
    TextView tvSelectedMonth, tvTotal;

    public ExpensesFragment() {
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expenses, container, false);

        db = AppDatabase.getDatabase(getContext());
        dao = db.appDao();
        formattedMonthName = new SimpleDateFormat("MMMM, yyyy");

        expenseAdapter = new ExpenseAdapter();
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewExpenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(expenseAdapter);
        FloatingActionButton fabAddExpense = rootView.findViewById(R.id.fabAddExpense);
        TextView tvChangeMonth = rootView.findViewById(R.id.tvChangeMonth);
        tvSelectedMonth = rootView.findViewById(R.id.tvSelectedMonth);
        tvTotal = rootView.findViewById(R.id.tvTotal);

        fabAddExpense.setOnClickListener(view -> {
            AddExpensesDialog.showDialog(getContext(), expenses -> {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    dao.addExpenses(expenses);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> setDate(), 200);
                });
            });
        });

        tvChangeMonth.setOnClickListener(view -> {
            showMonthPicker();
        });

        setDate();


        return rootView;
    }

    private void showMonthPicker() {
        MonthPickerDialog monthPickerDialog = new MonthPickerDialog();
        monthPickerDialog.setOnMonthSetListener((year, month) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            setDate();
        });

        monthPickerDialog.show(getActivity().getSupportFragmentManager(), "MonthPickerDialog");
    }

    void setDate() {

        String currentMonth = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
        String currentYear = String.valueOf(calendar.get(Calendar.YEAR));

        tvSelectedMonth.setText(formattedMonthName.format(calendar.getTime()));
        dao.getExpensesForMonthAndYear(SharedPreferencesUtils.getUser(getContext()), currentMonth, currentYear).observe(getViewLifecycleOwner(), expenses -> {
            expenseAdapter.submitList(new ArrayList<>());
            expenseAdapter.submitList(expenses);
            long totalAmount = expenses.stream().mapToLong(Expenses::getAmount).sum();
            if (totalAmount > 0) {
                tvTotal.setText("Total Expenses: $" + totalAmount);
            } else {
                tvTotal.setText("");
            }
        });

    }

}
