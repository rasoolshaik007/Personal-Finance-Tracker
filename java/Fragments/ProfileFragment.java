package edu.nwmissouri.personalfinancetracker.fragments;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.concurrent.atomic.AtomicReference;

import edu.nwmissouri.personalfinancetracker.R;
import edu.nwmissouri.personalfinancetracker.data.AppDao;
import edu.nwmissouri.personalfinancetracker.data.AppDatabase;
import edu.nwmissouri.personalfinancetracker.dialogs.InputDialog;
import edu.nwmissouri.personalfinancetracker.helper.SharedPreferencesUtils;

public class ProfileFragment extends Fragment {

    AppDatabase db;
    AppDao dao;
    String userEmail;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        db = AppDatabase.getDatabase(this.getActivity());
        dao = db.appDao();
        userEmail = SharedPreferencesUtils.getUser(getContext());

        // Find the TextView elements in your layout
        TextView firstNameTextView = rootView.findViewById(R.id.textFirstName);
        TextView lastNameTextView = rootView.findViewById(R.id.textLastName);
        TextView emailTextView = rootView.findViewById(R.id.textEmail);
        TextView mobileTextView = rootView.findViewById(R.id.textMobile);

        Button btnUpdateFirstName = rootView.findViewById(R.id.btnUpdateFirstName);
        Button btnUpdateLastName = rootView.findViewById(R.id.btnUpdateLastName);
        Button btnUpdateMobile = rootView.findViewById(R.id.btnUpdateMobile);

        AtomicReference<String> firstName = new AtomicReference<>("");
        AtomicReference<String> lastName = new AtomicReference<>("");
        AtomicReference<String> email = new AtomicReference<>("");
        AtomicReference<String> mobileNumber = new AtomicReference<>("");

        dao.getUserDetails(SharedPreferencesUtils.getUser(getContext())).observe(this.getViewLifecycleOwner(), user -> {
            firstName.set(user.getFirstName());
            lastName.set(user.getLastName());
            email.set(user.getEmail());
            mobileNumber.set(user.getMobile());

            firstNameTextView.setText(firstName.get());
            lastNameTextView.setText(lastName.get());
            emailTextView.setText(email.get());
            mobileTextView.setText(mobileNumber.get());

        });

        btnUpdateFirstName.setOnClickListener(view -> {
            InputDialog.showInputDialog(getContext(), "Update First Name", firstName.get(), InputType.TYPE_CLASS_TEXT, text -> {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    dao.updateFirstName(text, userEmail);
                });
                Toast.makeText(getContext(), "Updated", Toast.LENGTH_LONG).show();
            });
        });

        btnUpdateLastName.setOnClickListener(view -> {
            InputDialog.showInputDialog(getContext(), "Update Last Name", lastName.get(), InputType.TYPE_CLASS_TEXT, text -> {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    dao.updateLastName(text, userEmail);
                });
                Toast.makeText(getContext(), "Updated", Toast.LENGTH_LONG).show();
            });
        });

        btnUpdateMobile.setOnClickListener(view -> {
            InputDialog.showInputDialog(getContext(), "Update Mobile Number", mobileNumber.get(), InputType.TYPE_CLASS_NUMBER, text -> {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    dao.updateMobileNumber(text, userEmail);
                });
                Toast.makeText(getContext(), "Updated", Toast.LENGTH_LONG).show();
            });
        });


        return rootView;
    }
}
