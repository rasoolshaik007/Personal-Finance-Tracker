package edu.nwmissouri.personalfinancetracker.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import edu.nwmissouri.personalfinancetracker.MainActivity;
import edu.nwmissouri.personalfinancetracker.Manager.DatabaseManager;
import edu.nwmissouri.personalfinancetracker.R;
import edu.nwmissouri.personalfinancetracker.data.AppDatabase;
import edu.nwmissouri.personalfinancetracker.data.AppDao;
import edu.nwmissouri.personalfinancetracker.data.User;
import edu.nwmissouri.personalfinancetracker.helper.Utils;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private EditText etEmail, etPassword, etFirstname, etLastName, etMobileNumber, etRepeatPassword;

    AppDatabase db;
    AppDao dao;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = AppDatabase.getDatabase(getApplicationContext());
        dao = db.appDao();

        // Hide the action bar (title bar)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        findViewById(R.id.ivBack).setOnClickListener((View view) -> finish());
        findViewById(R.id.llLogin).setOnClickListener(this::onClickOnLogin);
        findViewById(R.id.btnLogin).setOnClickListener(this::onClickOnSignup);
        findViewById(R.id.ivShowPassword).setOnClickListener(view -> onClickOnShowPassword(etPassword, (ImageView) view));
        findViewById(R.id.ivShowRepeatPassword).setOnClickListener(view -> onClickOnShowPassword(etRepeatPassword, (ImageView) view));


        etEmail = findViewById(R.id.etEmail);
        etFirstname = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPassword = findViewById(R.id.etPassword);
        etRepeatPassword = findViewById(R.id.etRepeatPassword);
        etMobileNumber = findViewById(R.id.etMobile);
    }

    private void onClickOnShowPassword(EditText editText, ImageView imageView) {
        if (editText.getInputType() == 129) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView.setImageDrawable(getDrawable(R.drawable.ic_hide_password));
        } else {
            editText.setInputType(129);
            imageView.setImageDrawable(getDrawable(R.drawable.ic_show_password));
        }
        editText.setSelection(editText.length());
    }

    private void onClickOnLogin(View view) {
        finish();
    }

    private void onClickOnSignup(View view) {

        if (etFirstname.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter First Name", Toast.LENGTH_LONG).show();
            return;
        }

        if (etLastName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Last Name", Toast.LENGTH_LONG).show();
            return;
        }

        if (!Utils.isValidEmail(etEmail.getText().toString())) {
            Toast.makeText(this, "Enter Valid Email Address", Toast.LENGTH_LONG).show();
            return;
        }

        if (etPassword.getText().length() <= 3 || etPassword.getText().length() >= 16) {
            Toast.makeText(this, "Password length should be minimum 4 and maximum 16 characters long", Toast.LENGTH_LONG).show();
            return;
        }
//        if (!Utils.isValidPassword(etPassword.getText().toString())) {
//            Toast.makeText(this, "Password should contains at least one uppercase, lowercase, special character and number", Toast.LENGTH_LONG).show();
//            return;
//        }
        if (!etPassword.getText().toString().equals(etRepeatPassword.getText().toString())) {
            Toast.makeText(this, "Password and Confirm password should match", Toast.LENGTH_LONG).show();
            return;
        }

        if (etMobileNumber.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_LONG).show();
            return;
        }

       /* DatabaseManager.getInstance().setEmail(etEmail.getText().toString());
        DatabaseManager.getInstance().setMobile(etMobileNumber.getText().toString());
         DatabaseManager.getInstance().setFirstName(etFirstname.getText().toString());
        DatabaseManager.getInstance().setLastName(etLastName.getText().toString());
        createUser(etEmail.getText().toString(), etPassword.getText().toString());*/

        dao.checkEmailExists(etEmail.getText().toString()).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer > 0) {
                    Toast.makeText(RegisterActivity.this, "Email already exist", Toast.LENGTH_LONG).show();
                } else {
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        dao.registerUser(new User(
                                etEmail.getText().toString(),
                                etFirstname.getText().toString(),
                                etLastName.getText().toString(),
                                etPassword.getText().toString(),
                                etMobileNumber.getText().toString()
                        ));
                    });
                    Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finishAffinity();
                }
            }
        });


    }
}