package edu.nwmissouri.personalfinancetracker.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = "expenses")
public class Expenses {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String email;
    private String category;
    private long amount;
    private Date date;


    public Expenses(String email, String category, long amount, Date date) {
        this.email = email;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Expenses expenses = (Expenses) obj;

        if (id != expenses.id) return false;
        if (amount != expenses.amount) return false;
        if (!Objects.equals(category, expenses.category)) return false;
        return Objects.equals(date, expenses.date);
    }
}
