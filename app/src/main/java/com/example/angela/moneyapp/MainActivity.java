package com.example.angela.moneyapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //private ConstraintLayout background;
    private ArrayList<Person> people;
    private ArrayAdapter<Person> peopleAdapter;
    private ListView peopleListView;
    private Button addPersonButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //test

        wiringWidgets();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        addPersonButton.setOnClickListener(this);
    }

    private void wiringWidgets() {
        //background = (ConstraintLayout) findViewById(R.id.layout_main_background);
        //background.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        peopleListView = (ListView) findViewById(R.id.listview_people);
        addPersonButton = (Button) findViewById(R.id.button_add_person);
    }

    private void addPerson() {
        //startActivity(new Intent(MainActivity.this, AddPersonPopUp.class));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_add_person:
                break;
            default:
                break;
        }
    }
}
