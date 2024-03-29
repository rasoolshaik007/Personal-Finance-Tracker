package edu.nwmissouri.personalfinancetracker.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import edu.nwmissouri.personalfinancetracker.R;
import edu.nwmissouri.personalfinancetracker.data.Expenses;

public class ExpenseAdapter extends ListAdapter<Expenses, ExpenseAdapter.ViewHolder> {

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public ExpenseAdapter() {
        super(diffCallback);
    }

    private static final DiffUtil.ItemCallback<Expenses> diffCallback = new DiffUtil.ItemCallback<Expenses>() {
        @Override
        public boolean areItemsTheSame(@NonNull Expenses oldItem, @NonNull Expenses newItem) {
            return oldItem.getId() == newItem.getId(); // Assuming there's an ID field in Expenses class
        }

        @Override
        public boolean areContentsTheSame(@NonNull Expenses oldItem, @NonNull Expenses newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expenses expenseItem = getItem(position);
        holder.categoryTextView.setText(expenseItem.getCategory());
        holder.amountTextView.setText("$" + expenseItem.getAmount());
        holder.dateTextView.setText(dateFormat.format(expenseItem.getDate()));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTextView;
        TextView amountTextView;
        TextView dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.textViewCategory);
            amountTextView = itemView.findViewById(R.id.textViewAmount);
            dateTextView = itemView.findViewById(R.id.textViewDate);
        }
    }
}
