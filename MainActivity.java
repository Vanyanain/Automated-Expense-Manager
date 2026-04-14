package com.smartwallet.expense;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.smartwallet.expense.data.Expense;
import com.smartwallet.expense.data.ExpenseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private PieChart pieChart;
    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;
    
    private List<Expense> allExpenses = new ArrayList<>();
    private String currentTimeframe = "All Time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pieChart = findViewById(R.id.pieChart);
        pieChart.setNoDataText("No expenses logged yet. Tap the camera to scan a receipt!");
        pieChart.getDescription().setEnabled(false);

        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setTextSize(14f);
        legend.setFormSize(14f);
        legend.setXEntrySpace(10f);
        legend.setYEntrySpace(8f);
        legend.setWordWrapEnabled(true);
        
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new ExpenseAdapter();
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabAddManual = findViewById(R.id.fabAddManual);
        fabAddManual.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ExpenseDetailActivity.class));
        });

        FloatingActionButton fabAddExpense = findViewById(R.id.fabAddExpense);
        fabAddExpense.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CameraActivity.class));
        });

        Spinner spinnerTimeframe = findViewById(R.id.spinnerTimeframe);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"All Spending", "Weekly", "Monthly", "Yearly"});
        spinnerTimeframe.setAdapter(spinnerAdapter);
        spinnerTimeframe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentTimeframe = spinnerAdapter.getItem(position);
                refreshUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ExpenseDatabase.getDatabase(this).expenseDao().getAllExpenses().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenses) {
                allExpenses = expenses;
                refreshUI();
            }
        });
    }

    private void refreshUI() {
        if (allExpenses == null) return;
        
        long cutoffTime = 0;
        long now = System.currentTimeMillis();
        long dayMillis = 24L * 60 * 60 * 1000;
        
        if ("Weekly".equals(currentTimeframe)) {
            cutoffTime = now - (7 * dayMillis);
        } else if ("Monthly".equals(currentTimeframe)) {
            cutoffTime = now - (30 * dayMillis);
        } else if ("Yearly".equals(currentTimeframe)) {
            cutoffTime = now - (365 * dayMillis);
        }

        List<Expense> filtered = new ArrayList<>();
        for (Expense e : allExpenses) {
            if (e.dateInMillis >= cutoffTime) {
                filtered.add(e);
            }
        }

        adapter.setExpenses(filtered);
        updateChart(filtered);
    }

    private void updateChart(List<Expense> expenses) {
        if (expenses == null || expenses.isEmpty()) {
            showEmptyChart();
            return;
        }

        Map<String, Float> categoryTotals = new HashMap<>();
        float totalSum = 0f;
        for (Expense e : expenses) {
            float current = categoryTotals.getOrDefault(e.category, 0f);
            float amt = (float) e.amount;
            categoryTotals.put(e.category, current + amt);
            totalSum += amt;
        }

        if (totalSum <= 0f) {
            showEmptyChart();
            return;
        }

        pieChart.setCenterText("");

        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
            if (entry.getValue() > 0) {
                entries.add(new PieEntry(entry.getValue(), entry.getKey()));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expenses");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void showEmptyChart() {
        List<PieEntry> emptyEntries = new ArrayList<>();
        emptyEntries.add(new PieEntry(1f, "No Data"));
        PieDataSet emptyDataSet = new PieDataSet(emptyEntries, "");
        emptyDataSet.setColor(android.graphics.Color.LTGRAY);
        emptyDataSet.setDrawValues(false);
        PieData emptyData = new PieData(emptyDataSet);
        pieChart.setData(emptyData);
        pieChart.setCenterText("Total spending: $0.00\nAdd expenses to see breakdown");
        pieChart.invalidate();
    }
}
