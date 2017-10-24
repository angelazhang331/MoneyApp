package com.example.angela.moneyapp;

/**
 * Created by per6 on 10/24/17.
 */
//change
public class Owe {
    private int amount;
    private String date, description;
    private boolean isPaid;

    public Owe(int amount, String date, String description) {
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public Owe(int amount, String date) {
        this.amount = amount;
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
