package com.smartwallet.expense;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartwallet.expense.data.Expense;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenses = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    public void setExpenses(List<Expense> newExpenses) {
        this.expenses = newExpenses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.tvMerchant.setText(expense.merchantName);
        
        String dateString = sdf.format(new Date(expense.dateInMillis));
        holder.tvCategoryDate.setText(expense.category + " • " + dateString);
        
        holder.tvAmount.setText(String.format(Locale.US, "$%.2f", expense.amount));
        
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ExpenseDetailActivity.class);
            intent.putExtra("ID", expense.id);
            intent.putExtra("MERCHANT", expense.merchantName);
            intent.putExtra("AMOUNT", expense.amount);
            intent.putExtra("DATE", dateString);
            intent.putExtra("CATEGORY", expense.category);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvMerchant, tvCategoryDate, tvAmount;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMerchant = itemView.findViewById(R.id.tvMerchant);
            tvCategoryDate = itemView.findViewById(R.id.tvCategoryDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}
