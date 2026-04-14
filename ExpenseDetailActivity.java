package com.smartwallet.expense;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import com.smartwallet.expense.data.Expense;
import com.smartwallet.expense.data.ExpenseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExpenseDetailActivity extends AppCompatActivity {

    private long selectedDateMillis;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private int expenseId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        TextInputEditText etMerchant = findViewById(R.id.etMerchant);
        TextInputEditText etAmount = findViewById(R.id.etAmount);
        TextInputEditText etDate = findViewById(R.id.etDate);
        TextInputEditText etCategory = findViewById(R.id.etCategory);
        Button btnSave = findViewById(R.id.btnSave);

        expenseId = getIntent().getIntExtra("ID", -1);
        String merchant = getIntent().getStringExtra("MERCHANT");
        double amount = getIntent().getDoubleExtra("AMOUNT", -1.0);
        String dateString = getIntent().getStringExtra("DATE");
        String category = getIntent().getStringExtra("CATEGORY");

        if (merchant != null) etMerchant.setText(merchant);
        if (amount >= 0) etAmount.setText(String.valueOf(amount));
        if (category != null) etCategory.setText(category);
        
        if (expenseId != -1) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Edit Expense");
            }
        }

        // Pre-parse the OCR date or use current time as fallback
        selectedDateMillis = System.currentTimeMillis();
        if (dateString != null && !dateString.isEmpty()) {
            try {
                Date parsedDate = sdf.parse(dateString);
                if (parsedDate != null) {
                    selectedDateMillis = parsedDate.getTime();
                }
            } catch (ParseException e) {
                dateString = sdf.format(new Date(selectedDateMillis));
            }
        } else {
            dateString = sdf.format(new Date(selectedDateMillis));
        }
        etDate.setText(dateString);

        // Set up the DatePickerDialog logic when tapping the field
        etDate.setFocusable(false);
        etDate.setClickable(true);
        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selectedDateMillis);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(ExpenseDetailActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year1, monthOfYear, dayOfMonth);
                        selectedDateMillis = newDate.getTimeInMillis();
                        etDate.setText(sdf.format(newDate.getTime()));
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Insert or Update database transaction safely
        btnSave.setOnClickListener(v -> {
            Expense e = new Expense(
                etMerchant.getText() != null ? etMerchant.getText().toString() : "",
                Double.parseDouble(etAmount.getText() != null && !etAmount.getText().toString().isEmpty() ? etAmount.getText().toString() : "0"),
                selectedDateMillis,
                etCategory.getText() != null ? etCategory.getText().toString() : ""
            );

            new Thread(() -> {
                if (expenseId != -1) {
                    e.id = expenseId;
                    ExpenseDatabase.getDatabase(this).expenseDao().update(e);
                } else {
                    ExpenseDatabase.getDatabase(this).expenseDao().insert(e);
                }
                runOnUiThread(this::finish);
            }).start();
        });
    }
}
