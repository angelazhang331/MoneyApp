package com.example.angela.moneyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PersonActivity extends AppCompatActivity {

    private String personName;
    private TextView nameTextView;
    private ListView oweListView;
    private ArrayList<Owe> oweList;
    private ArrayAdapter<Owe> oweAdapter;
    private Person currentPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        wiringWidgets();

//        adapter = new ArrayAdapter<>(this, R.layout.list_item_color, colorList);
//        colorListView.setAdapter(adapter);
////        setListItemColors();
//        colorListView.setOnItemClickListener(new ListView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
//                Intent i = new Intent(MainActivity.this, SpecificActivity.class);
//                //get the object from teh ArrayList and put it in the extra for the Intent
//                i.putExtra(EXTRA_NAME, colorList.get(pos));
//                startActivity(i);
//            }
//        });

        Intent get = getIntent();
        personName = get.getStringExtra(MainActivity.EXTRA_KEY);
        currentPerson = get.getParcelableExtra(MainActivity.EXTRA_KEY);
        oweList = currentPerson.getOweList();
        adaptArray();

    }

    private void adaptArray() {
        oweListView.setAdapter(oweAdapter);
        oweListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                //TODO make the popup to add an Owe
            }
        });
    }

    private void wiringWidgets() {
        nameTextView = (TextView) findViewById(R.id.textView_name);
        oweListView = (ListView) findViewById(R.id.listView_owes);
    }
}
