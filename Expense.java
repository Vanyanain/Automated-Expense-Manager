package com.smartwallet.expense.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String merchantName;
    public double amount;
    public long dateInMillis;
    public String category;

    public Expense(String merchantName, double amount, long dateInMillis, String category) {
        this.merchantName = merchantName;
        this.amount = amount;
        this.dateInMillis = dateInMillis;
        this.category = category;
    }
}
