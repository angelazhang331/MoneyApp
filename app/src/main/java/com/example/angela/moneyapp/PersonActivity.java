package com.example.angela.moneyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PersonActivity extends AppCompatActivity implements View.OnClickListener {

    private String personName;
    private TextView nameTextView;
    private ListView oweListView;
    private ArrayList<Owe> oweList;
    private ArrayAdapter<Owe> oweAdapter;
    private Person currentPerson;
    private Button addAmountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        wiringWidgets();
        setOnClickListeners();

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
        nameTextView.setText(personName);
        currentPerson = get.getParcelableExtra(MainActivity.EXTRA_KEY);
        oweList = currentPerson.getOweList();
        adaptArray();

    }

    private void setOnClickListeners() {
        addAmountButton.setOnClickListener(this);
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
        addAmountButton = (Button) findViewById(R.id.button_new_amount);
    }

    @Override
    public void onClick(View view) {
        addAmount();
    }

    private void addAmount() {
        AlertDialog.Builder addAmountBuilder = new AlertDialog.Builder(PersonActivity.this);
        View addAmountView = getLayoutInflater().inflate(R.layout.activity_pop_up_add_amount, null);
        final AlertDialog dialog = addAmountBuilder.create();

        //Wiring the dialog widgets
//        final EditText etName = (EditText) addPersonView.findViewById(R.id.editText_pop_up_add_name);
//        final EditText etAmountOwed = (EditText) addPersonView.findViewById(R.id.editText_pop_up_add_person_amount_owed);
//        final EditText etDate = (EditText) addPersonView.findViewById(R.id.editText_pop_up_add_person_date);
//        final EditText etDescription = (EditText) addPersonView.findViewById(R.id.editText_pop_up_add_person_description);
//        Button submit = (Button) addPersonView.findViewById(R.id.button_pop_up_add_person_submit);
//        Button cancel = (Button) addPersonView.findViewById(R.id.button_pop_up_add_person_cancel);
//
//        /**
//         * When submitted, we'll convert the data of our widget variables to be applicable for the 'Person' class
//         * The dialog will not accept an empty name field, all other fields are optional
//         */
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!etName.getText().toString().isEmpty()){ //if the name field is NOT empty
//
//                    String sName = etName.getText().toString();
//                    String sAmountOwed = etAmountOwed.getText().toString();
//                    String sDate = etDate.getText().toString();
//                    String sDescription = etDescription.getText().toString();
//                    int iAmountOwed = 0;
//
//                    if (!sAmountOwed.equals("")) {
//                        //Second, convert our amount owed to a double
//                        iAmountOwed = Integer.parseInt(sAmountOwed);
//                    }
//                    //First, convert the EditText to String variables
//                    if (!sDescription.equals("")) {
//                        if (sDate.equals("")) {
//                            Toast.makeText(MainActivity.this, "Please enter a date", Toast.LENGTH_SHORT).show();
//                        }
//                        else {
//                            peopleList.add(new Person(sName, sDescription, sDate, iAmountOwed));
//                        }
//                    }
//                    else {
//                        if(!sDate.equals("")){
//                            peopleList.add(new Person(sName));
//                        }
//                        else {
//                            peopleList.add(new Person(sName, sDate, iAmountOwed));
//                        }
//                    }
//                    dialog.dismiss();
//                }
//                else {
//                    Toast.makeText(MainActivity.this, "Please fill in the name field", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.setView(addPersonView);
//        dialog.show();
    }
}
