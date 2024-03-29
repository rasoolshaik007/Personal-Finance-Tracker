package edu.nwmissouri.personalfinancetracker;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import edu.nwmissouri.personalfinancetracker.data.AppDao;
import edu.nwmissouri.personalfinancetracker.data.AppDatabase;
import edu.nwmissouri.personalfinancetracker.data.Expenses;
import edu.nwmissouri.personalfinancetracker.dialogs.MonthPickerDialog;
import edu.nwmissouri.personalfinancetracker.helper.SharedPreferencesUtils;


public class ActivityChart extends AppCompatActivity {

    private static final String TAG = "ActivityChart";

    Calendar calendar = Calendar.getInstance();

    TextView tvSelectedMonth;

    Button changeMonth;

    AppDatabase db;
    AppDao dao;
    String userEmail;
    SimpleDateFormat formattedMonthName;

    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        db = AppDatabase.getDatabase(this);
        dao = db.appDao();
        userEmail = SharedPreferencesUtils.getUser(this);
        formattedMonthName = new SimpleDateFormat("MMMM, yyyy");

        // Hide the action bar (title bar)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        findViewById(R.id.ivBack).setOnClickListener((View view) -> finish());

        tvSelectedMonth = findViewById(R.id.tvSelectedMonth);
        changeMonth = findViewById(R.id.btChangeMonth);
        pieChart = findViewById(R.id.pieChart);


        setDate();
        changeMonth.setOnClickListener(view -> {
            MonthPickerDialog monthPickerDialog = new MonthPickerDialog();
            monthPickerDialog.setOnMonthSetListener((year, month) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month - 1);
                setDate();
            });
            monthPickerDialog.show(getSupportFragmentManager(), "MonthPickerDialog");
        });
    }

    void setDate() {

        String currentMonth = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
        String currentYear = String.valueOf(calendar.get(Calendar.YEAR));

        tvSelectedMonth.setText(formattedMonthName.format(calendar.getTime()));
        dao.getExpensesForMonthAndYear(SharedPreferencesUtils.getUser(this), currentMonth, currentYear).observe(this, expenses -> {
            showChart(expenses);
        });

    }

    void showChart(List<Expenses> expenses) {

        long totalAmount = expenses.stream().mapToLong(Expenses::getAmount).sum();

        HashMap<String, Long> categoryAmountMap = new HashMap<>();
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Expenses expense : expenses) {
            String category = expense.getCategory();
            long amount = expense.getAmount();
            if (categoryAmountMap.containsKey(category)) {
                categoryAmountMap.put(category, categoryAmountMap.get(category) + amount);
            } else {
                categoryAmountMap.put(category, amount);
            }
        }
        for (String category : categoryAmountMap.keySet()) {
            long categoryAmount = categoryAmountMap.get(category);
            float percentage = (float) categoryAmount / totalAmount * 100;

            entries.add(new PieEntry(percentage, category));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieData.setValueTextSize(14f);
        pieData.setValueTextColor(Color.WHITE);
        dataSet.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        if (totalAmount > 0) {
            pieChart.setCenterText("Expenses");
        }else{
            pieChart.setCenterText("No Expenses Found");
        }
        pieChart.animateY(1000);
        pieChart.invalidate(); // Refresh the chart
    }


}