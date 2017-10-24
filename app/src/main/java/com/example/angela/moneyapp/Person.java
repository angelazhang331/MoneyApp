package com.example.angela.moneyapp;

import java.util.ArrayList;

/**
 * Created by Angela on 10/20/17.
 */

public class Person {

    //instance variables
    private String name;
    private ArrayList<Owe> oweList;

    //constructors
    public Person(String name, String description, String date, int amount) {
        this.name = name;
        oweList = new ArrayList<>();
        addOwe(date, description, amount);
    }

    public Person(String name, String date, int amount) {
        this.name = name;
        oweList = new ArrayList<>();
        addOwe(date, amount);
    }

    public Person(String name) {
        this.name = name;
        oweList = new ArrayList<>();
    }

    //methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addOwe(String date, String description, int amount) {
        Owe initial = new Owe(amount, date, description);
        oweList.add(initial);
    }

    public void addOwe(String date, int amount) {
        Owe initial = new Owe( amount, date);
        oweList.add(initial);
    }
}

