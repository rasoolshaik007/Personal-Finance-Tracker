package edu.nwmissouri.personalfinancetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import edu.nwmissouri.personalfinancetracker.auth.LoginActivity;
import edu.nwmissouri.personalfinancetracker.data.AppDao;
import edu.nwmissouri.personalfinancetracker.data.AppDatabase;
import edu.nwmissouri.personalfinancetracker.data.FinanceDetails;
import edu.nwmissouri.personalfinancetracker.fragments.ExpensesFragment;
import edu.nwmissouri.personalfinancetracker.fragments.ProfileFragment;
import edu.nwmissouri.personalfinancetracker.fragments.SalaryFragment;
import edu.nwmissouri.personalfinancetracker.helper.SharedPreferencesUtils;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Fragment salaryFragment;
    private Fragment expensesFragment;
    private Fragment profileFragment;
    ImageView iv;

    AppDatabase db;
    AppDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getDatabase(getApplicationContext());
        dao = db.appDao();

        iv = findViewById(R.id.ivLogout);

        // Initialize fragments
        salaryFragment = new SalaryFragment();
        expensesFragment = new ExpensesFragment();
        profileFragment = new ProfileFragment();

        // Hide the action bar (title bar)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize and set up the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            bottomNavigationView.getMenu().findItem(R.id.menu_salary).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.menu_expenses).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.menu_profile).setChecked(false);

            int itemId = item.getItemId();
            if (itemId == R.id.menu_salary) {
                loadFragment(salaryFragment);
                item.setChecked(true);
                return true;
            } else if (itemId == R.id.menu_expenses) {
                loadFragment(expensesFragment);
                item.setChecked(true);
                return true;
            } else if (itemId == R.id.menu_profile) {
                loadFragment(profileFragment);
                item.setChecked(true);
                return true;
            }
            return false;
        });

        // Set the initial fragment
        loadFragment(salaryFragment);
        setupViews();

        iv.setOnClickListener(view -> {
            SharedPreferencesUtils.clearAll(this);
            startActivity(new Intent(MainActivity.this, SplashScreen.class));
            finishAffinity();
        });

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    private void setupViews() {
        // findViewById(R.id.ivLogout).setOnClickListener(this::onClickOnLogout);
    }

    private void onClickOnLogout(View v) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

}

