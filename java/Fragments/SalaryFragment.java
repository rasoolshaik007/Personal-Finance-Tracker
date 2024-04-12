package edu.nwmissouri.personalfinancetracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.concurrent.atomic.AtomicReference;

import edu.nwmissouri.personalfinancetracker.ActivityChart;
import edu.nwmissouri.personalfinancetracker.R;
import edu.nwmissouri.personalfinancetracker.data.AppDao;
import edu.nwmissouri.personalfinancetracker.data.AppDatabase;
import edu.nwmissouri.personalfinancetracker.helper.AlertHelper;
import edu.nwmissouri.personalfinancetracker.dialogs.InputDialog;
import edu.nwmissouri.personalfinancetracker.helper.SharedPreferencesUtils;

public class SalaryFragment extends Fragment {

    AppDatabase db;
    AppDao dao;
    String userEmail;

    public SalaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_salary, container, false);

        db = AppDatabase.getDatabase(this.getActivity());
        dao = db.appDao();
        userEmail = SharedPreferencesUtils.getUser(getContext());

        Button viewExpensesButton = rootView.findViewById(R.id.viewExpenses);
        Button btnUpdateSalary = rootView.findViewById(R.id.btnUpdateSalary);
        EditText monthlySalary = rootView.findViewById(R.id.editTextMonthlySalary);
        TextView rd = rootView.findViewById(R.id.rd);
        TextView fd = rootView.findViewById(R.id.fd);
        TextView stocks = rootView.findViewById(R.id.stocks);
        Button btUpdateFd = rootView.findViewById(R.id.btUpdateFd);
        Button btUpdateRd = rootView.findViewById(R.id.btUpdateRd);
        Button btUpdateStocks = rootView.findViewById(R.id.btUpdateStocks);


        viewExpensesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityChart.class);
                startActivity(intent);
            }
        });

        btnUpdateSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input = monthlySalary.getText().toString();

                if (input.isEmpty()) {
                    AlertHelper.showOkAlert(v.getContext(), "Error", "Please enter salary");
                } else {
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        dao.updateSalary(Long.valueOf(input), userEmail);
                    });
                    Toast.makeText(getContext(), "Salary Updated", Toast.LENGTH_LONG).show();
                }
            }
        });

        AtomicReference<String> rdValue = new AtomicReference<>("0");
        AtomicReference<String> fdValue = new AtomicReference<>("0");
        AtomicReference<String> stockValue = new AtomicReference<>("0");


        dao.getFinanceDetails(SharedPreferencesUtils.getUser(getContext())).observe(this.getViewLifecycleOwner(), financeDetails -> {

            rdValue.set(String.valueOf(financeDetails.getRd()));
            fdValue.set(String.valueOf(financeDetails.getFd()));
            stockValue.set(String.valueOf(financeDetails.getStock()));

            rd.setText("Required deposit (RD): $" + rdValue.get());
            fd.setText("FD (Fixed Deposit): $" + fdValue.get());
            stocks.setText("Stocks: $" + stockValue.get());
            monthlySalary.setText("" + financeDetails.getSalary());
        });

        btUpdateRd.setOnClickListener(view -> {
            InputDialog.showInputDialog(getContext(), "Update Required deposit (RD)", rdValue.get(), InputType.TYPE_CLASS_NUMBER, number -> {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    dao.updateRD(Long.valueOf(number), userEmail);
                });
                Toast.makeText(getContext(), "RD Updated", Toast.LENGTH_LONG).show();
            });
        });

        btUpdateFd.setOnClickListener(view -> {
            InputDialog.showInputDialog(getContext(), "Update FD (Fixed Deposit)", fdValue.get(), InputType.TYPE_CLASS_NUMBER, number -> {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    dao.updateFD(Long.valueOf(number), userEmail);
                });
                Toast.makeText(getContext(), "FD Updated", Toast.LENGTH_LONG).show();
            });
        });

        btUpdateStocks.setOnClickListener(view -> {
            InputDialog.showInputDialog(getContext(), "Update Stocks", stockValue.get(), InputType.TYPE_CLASS_NUMBER, number -> {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    dao.updateStocks(Long.valueOf(number), userEmail);
                });
                Toast.makeText(getContext(), "Stocks Updated", Toast.LENGTH_LONG).show();
            });
        });


        return rootView;
    }
}

