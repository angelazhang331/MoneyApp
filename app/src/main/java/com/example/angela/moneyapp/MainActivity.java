package com.example.angela.moneyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //private ConstraintLayout background;
    private ArrayList<Person> peopleList;
    private ArrayAdapter<Person> peopleAdapter;
    private ListView peopleListView;
    private Button addPersonButton;
    public static final String EXTRA_KEY = "key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //test

        wiringWidgets();
        setOnClickListeners();
        adaptingArrays();

    }

    private void adaptingArrays() {
        peopleAdapter = new ArrayAdapter<Person>(this, R.layout.list_item_single_person, peopleList);
        peopleListView.setAdapter(peopleAdapter);
        peopleListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent send = new Intent(MainActivity.this, PersonActivity.class);
                send.putExtra(EXTRA_KEY, peopleList.get(pos));
                startActivity(send);
            }
        });

    }

    private void setOnClickListeners() {
        addPersonButton.setOnClickListener(this);
    }

    private void wiringWidgets() {
        //background = (ConstraintLayout) findViewById(R.id.layout_main_background);
        //background.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        peopleListView = (ListView) findViewById(R.id.listView_people);
        addPersonButton = (Button) findViewById(R.id.button_add_person);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            //TODO change this to the first slot on the listview?
            case R.id.button_add_person:
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.activity_pop_up_add_person, null);
                final EditText mPersonName = (EditText) mView.findViewById(R.id.editText_pop_up_add_name);
                EditText mAmountOwed = (EditText) mView.findViewById(R.id.editText_pop_up_add_person_amount_owed);
                EditText mDate = (EditText) mView.findViewById(R.id.editText_pop_up_add_person_date);
                EditText mDescription = (EditText) mView.findViewById(R.id.editText_pop_up_add_person_description);
                Button mSubmit = (Button) mView.findViewById(R.id.button_pop_up_add_person_submit);

                mSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!mPersonName.getText().toString().isEmpty()){
                            Toast.makeText(MainActivity.this, "If this shows it worked", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "aiohufijuhaioj", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
                break;
            default:
                break;
        }
    }
}
