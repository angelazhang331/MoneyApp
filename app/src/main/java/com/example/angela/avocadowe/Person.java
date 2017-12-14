package com.example.angela.avocadowe;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by Angela on 10/20/17.
 */

public class Person implements Parcelable, Comparable<Person> {

    //instance variables
    private String name;
    private ArrayList<Owe> oweList;
    private int amount = 0;

    //constructors
    public Person(String name, String description, String date, int amount) {
        this.name = name;
        oweList = new ArrayList<>();
        this.amount = amount;
        addOwe(date, description, this.amount);
    }

    public Person(String name, String date, int amount) {
        this.name = name;
        oweList = new ArrayList<>();
        this.amount = amount;
        addOwe(date, this.amount);
    }

    public Person(String name) {
        this.name = name;
        oweList = new ArrayList<>();
    }

    public Person(String name, ArrayList<Owe> oweList) {
        this.oweList = oweList;
        this.name = name;
    }

    //methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Owe> getOweList() {
        return oweList;
    }

    public void setOweList(ArrayList<Owe> oweList) {
        this.oweList = oweList;
    }

    public void addOwe(String date, String description, int amount) {
        Owe initial = new Owe(amount, date, description);
        oweList.add(initial);
    }

    public void addOwe(String date, int amount) {
        Owe initial = new Owe( amount, date);
        oweList.add(initial);
    }

    @Override
    public String toString() {
        return "   " + name;
    }



    protected Person(Parcel in) {
        name = in.readString();
        if (in.readByte() == 0x01) {
            oweList = new ArrayList<Owe>();
            in.readList(oweList, Owe.class.getClassLoader());
        } else {
            oweList = null;
        }
        amount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        if (oweList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(oweList);
        }
        dest.writeInt(amount);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int compareTo(@NonNull Person person) {
        return name.compareTo(person.name);
    }
}