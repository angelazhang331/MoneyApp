package com.example.angela.moneyapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //private ConstraintLayout background;
    private ArrayList<Person> people;
    private ArrayAdapter<Person> peopleAdapter;
    private ListView peopleListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //test

        wiringWidgets();
    }

    private void wiringWidgets() {
        //background = (ConstraintLayout) findViewById(R.id.layout_main_background);
        //background.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        peopleListView = (ListView) findViewById(R.id.listview_people);
    }
}
