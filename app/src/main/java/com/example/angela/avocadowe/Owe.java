package com.example.angela.avocadowe;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by per6 on 10/24/17.
 */
//change
public class Owe implements Parcelable {
    private int amount;
    private int amountPaid;
    private String date;
    private String description;
    private boolean isPaid;

    public Owe(int amount, String date, String description) {
        this.amount = amount;
        this.date = date;
        this.description = description;
        amountPaid = 0;
        isPaid = false;
    }

    public Owe(int amount, String date) {
        this.amount = amount;
        this.date = date;
        description = "";
        amountPaid = 0;
        isPaid = false;
    }

    public Owe(int amount, String date, String description, int amountPaid, boolean isPaid) {
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.amountPaid = amountPaid;
        this.isPaid = isPaid;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
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

    @Override
    public String toString() {
        return " " + date + " - " + amount;
    }

    protected Owe(Parcel in) {
            amount = in.readInt();
            amountPaid = in.readInt();
            date = in.readString();
            description = in.readString();
            isPaid = in.readByte() != 0x00;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(amount);
            dest.writeInt(amountPaid);
            dest.writeString(date);
            dest.writeString(description);
            dest.writeByte((byte) (isPaid ? 0x01 : 0x00));
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Owe> CREATOR = new Parcelable.Creator<Owe>() {
            @Override
            public Owe createFromParcel(Parcel in) {
                return new Owe(in);
            }

            @Override
            public Owe[] newArray(int size) {
                return new Owe[size];
            }
        };
    }