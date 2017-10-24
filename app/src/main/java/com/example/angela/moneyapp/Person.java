package com.example.angela.moneyapp;

/**
 * Created by Angela on 10/20/17.
 */

public class Person {

    //instance variables
    private String name;

    //constructors
    public Person(String name, String description, String date, int amount) {
        this.name = name;
//        this.description = description;
//        this.date = date;
//        this.amount = amount;
    }

    public Person(String name) {
        this.name = name;
    }

    //methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addOwe(String date, String description, int amount) {
        //Owe initial = new Owe( , , );
    }

    public void addOwe(String description, int amount) {
        //Owe initial = new Owe( , );
    }

    public void addOwe(int amount) {
        //Owe initial = new Owe();
    }

    //trying to fix something?

}

