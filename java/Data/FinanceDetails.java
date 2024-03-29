package edu.nwmissouri.personalfinancetracker.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "finance_details")
public class FinanceDetails {

    @PrimaryKey
    @NonNull
    private String email;
    private long rd;
    private long fd;
    private long stock;
    private long other;
    private long salary;

    public FinanceDetails(String email, long rd, long fd, long stock, long other, long salary) {
        this.email = email;
        this.rd = rd;
        this.fd = fd;
        this.stock = stock;
        this.other = other;
        this.salary = salary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getRd() {
        return rd;
    }

    public void setRd(long rd) {
        this.rd = rd;
    }

    public long getFd() {
        return fd;
    }

    public void setFd(long fd) {
        this.fd = fd;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public long getOther() {
        return other;
    }

    public void setOther(long other) {
        this.other = other;
    }
}
