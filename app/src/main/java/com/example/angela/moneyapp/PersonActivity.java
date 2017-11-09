package com.example.angela.moneyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PersonActivity extends AppCompatActivity implements View.OnClickListener {

    private String personName;
    private TextView nameTextView;
    private ListView oweListView;
    private ArrayList<Owe> oweList;
    private ArrayAdapter<Owe> oweAdapter;
    private Person currentPerson;
    private Button addAmountButton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor preferenceEditor;
    private Type type;
    private Gson gson = new Gson();
    private String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        wiringWidgets();
        setOnClickListeners();

        //shared preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        preferenceEditor  = sharedPreferences.edit();

        Intent get = getIntent();
        currentPerson = get.getParcelableExtra(MainActivity.EXTRA_KEY);
        personName = currentPerson.getName();
        nameTextView.setText(personName);

        if (!sharedPreferences.contains("MyOweArray")) {

            oweList = currentPerson.getOweList();
            json =  gson.toJson(oweList);
            preferenceEditor.putString("MyOweArray", json);
            preferenceEditor.commit();
        }
        else {
            //if already set, then just retrieve the array from the shared preferences
            type = new TypeToken<List<Owe>>(){}.getType();
            json = sharedPreferences.getString("MyOweArray", "");
            if (gson.fromJson(json, type) == null) {
                oweList = new ArrayList<>();
                //the things null
            }
            else {
                oweList = gson.fromJson(json, type);
                //the things not null
            }
        }

        adaptArray();

    }

    private void setOnClickListeners() {
        addAmountButton.setOnClickListener(this);
    }

    private void adaptArray() {
        oweAdapter = new ArrayAdapter<Owe>(this, R.layout.list_item_single_amount, oweList);
        oweListView.setAdapter(oweAdapter);
        oweListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                //what
                viewOwe(pos);
            }
        });
    }

    private void viewOwe(int pos) {
        AlertDialog.Builder viewOweBuilder = new AlertDialog.Builder(PersonActivity.this);
        View viewOweView = getLayoutInflater().inflate(R.layout.pop_up_view_owe, null);
        final AlertDialog dialog = viewOweBuilder.create();

        //Wiring the dialog widgets
        TextView dateTextView = (TextView) findViewById(R.id.textView_date);
        TextView amountOwedTextView = (TextView) findViewById(R.id.textView_amount_owed);
        TextView amountPaidTextView = (TextView) findViewById(R.id.textView_amount_paid);

        //Setting the textview text?
        dateTextView.setText(oweList.get(pos).getDate());
        amountOwedTextView.setText(oweList.get(pos).getAmount());
        amountPaidTextView.setText(oweList.get(pos).getAmountPaid());
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
//                    //First, convert the EditText to String variables
//                    String sName = etName.getText().toString();
//                    String sAmountOwed = etAmountOwed.getText().toString();
//                    String sDate = etDate.getText().toString();
//                    String sDescription = etDescription.getText().toString();
//                    int iAmountOwed = 0;
//
//                    if (!sAmountOwed.equals("")) {
//                        //Second, convert our amount owed to an integer
//                        iAmountOwed = Integer.parseInt(sAmountOwed);
//                    }
//
//                    if (!sDescription.equals("")) {
//                        if (sDate.equals("")) {
//                            Toast.makeText(MainActivity.this, "Please enter a date", Toast.LENGTH_SHORT).show();
//                        }
//                        else {
//                            peopleList.add(new Person(sName, sDescription, sDate, iAmountOwed));
//
//                            json =  gson.toJson(peopleList);
//                            preferenceEditor.putString("MyArray", json);
//                            preferenceEditor.apply();
//                        }
//                    }
//                    else {
//                        if(!sDate.equals("")){
//                            peopleList.add(new Person(sName));
//                            json =  gson.toJson(peopleList);
//                            preferenceEditor.putString("MyArray", json);
//                            preferenceEditor.apply();
//                        }
//                        else {
//                            peopleList.add(new Person(sName, sDate, iAmountOwed));
//
//                            json =  gson.toJson(peopleList);
//                            preferenceEditor.putString("MyArray", json);
//                            preferenceEditor.apply();
//                        }
//                    }
//                    dialog.dismiss();
//                    sortByName();
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
        View addAmountView = getLayoutInflater().inflate(R.layout.pop_up_add_amount, null);
        final AlertDialog dialog = addAmountBuilder.create();

        //Wiring the dialog widgets
        final EditText etAmountOwed = (EditText) addAmountView.findViewById(R.id.editText_pop_up_add_amount_amount_owed);
        final EditText etDate = (EditText) addAmountView.findViewById(R.id.editText_pop_up_add_amount_date);
        final EditText etDescription = (EditText) addAmountView.findViewById(R.id.editText_pop_up_add_amount_description);
        Button submit = (Button) addAmountView.findViewById(R.id.button_pop_up_add_amount_submit);
        Button cancel = (Button) addAmountView.findViewById(R.id.button_pop_up_add_amount_cancel);

        /**
         * When submitted, we'll convert the data of our widget variables to be applicable for the 'Person' class
         * The dialog will not accept an empty name field, all other fields are optional
         */
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etAmountOwed.getText().toString().isEmpty()){ //if the name field is NOT empty

                    String sAmountOwed = etAmountOwed.getText().toString();
                    int iAmountOwed = Integer.parseInt(sAmountOwed);
                    String sDate = etDate.getText().toString();
                    String sDescription = etDescription.getText().toString();

                    if (!sDescription.equals("")) {
                        if (sDate.equals("")) {
                            Toast.makeText(PersonActivity.this, "Please enter a date", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            oweList.add(new Owe(iAmountOwed, sDate, sDescription));

                            json =  gson.toJson(oweList);
                            preferenceEditor.putString("MyOweArray", json);
                            preferenceEditor.apply();
                        }
                    }
                    else {
                        if(!sDate.equals("")){
                            oweList.add(new Owe(iAmountOwed, sDate));

                            json =  gson.toJson(oweList);
                            preferenceEditor.putString("MyOweArray", json);
                            preferenceEditor.apply();
                        }
                        else {
                            Toast.makeText(PersonActivity.this, "Please enter a date", Toast.LENGTH_SHORT).show();
                        }
                    }
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(PersonActivity.this, "Please fill in the amount field", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setView(addAmountView);
        dialog.show();
    }
}
