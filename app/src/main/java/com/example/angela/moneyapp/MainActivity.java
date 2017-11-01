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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        peopleList = new ArrayList<Person>();
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

    private void addPerson() {
        AlertDialog.Builder addPersonBuilder = new AlertDialog.Builder(MainActivity.this);
        View addPersonView = getLayoutInflater().inflate(R.layout.activity_pop_up_add_person, null);
        final AlertDialog dialog = addPersonBuilder.create();

        //Wiring the dialog widgets
        final EditText etName = (EditText) addPersonView.findViewById(R.id.editText_pop_up_add_name);
        final EditText etAmountOwed = (EditText) addPersonView.findViewById(R.id.editText_pop_up_add_person_amount_owed);
        final EditText etDate = (EditText) addPersonView.findViewById(R.id.editText_pop_up_add_person_date);
        final EditText etDescription = (EditText) addPersonView.findViewById(R.id.editText_pop_up_add_person_description);
        Button submit = (Button) addPersonView.findViewById(R.id.button_pop_up_add_person_submit);
        Button cancel = (Button) addPersonView.findViewById(R.id.button_pop_up_add_person_cancel);

        /**
         * When submitted, we'll convert the data of our widget variables to be applicable for the 'Person' class
         * The dialog will not accept an empty name field, all other fields are optional
         */
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etName.getText().toString().isEmpty()){ //if the name field is NOT empty

                    String sName = etName.getText().toString();
                    String sAmountOwed = etAmountOwed.getText().toString();
                    String sDate = etDate.getText().toString();
                    String sDescription = etDescription.getText().toString();

                    //Second, convert our amount owed to a double
                    int iAmountOwed = Integer.parseInt(sAmountOwed);

                    //First, convert the EditText to String variables
                    if (!etDescription.getText().toString().isEmpty()) {
                        if (etDate.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this, "Please enter a date", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            peopleList.add(new Person(sName, sDescription, sDate, iAmountOwed));
                        }
                    }
                    else {
                        if(etDate.getText().toString().isEmpty()){
                            peopleList.add(new Person(sName));
                        }
                        else {
                            peopleList.add(new Person(sName, sDate, iAmountOwed));
                        }
                    }


                    //Third, we'll create a new Person and add them to the arrayList of people
                    //peopleList.add(new Person(sName, sDescription, sDate, iAmountOwed));
                    peopleList.add(new Person(sName));
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(MainActivity.this, "Please fill in the name field", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setView(addPersonView);
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_add_person: //TODO: we'll change this to the first slot on list view
                addPerson();
                break;
            default:
                break;
        }
    }
}


/*
Ideas
- Possibly make a fake Person object as the first item in the array list to use as an add Person button
- Would have to identify the position and the difference between this and others when writing intents to change screens

- To do only if there is extra time.
 */