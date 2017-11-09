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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //private ConstraintLayout background;
    private ArrayList<Person> peopleList;
    private ArrayAdapter<Person> peopleAdapter;
    private ListView peopleListView;
    private Button addPersonButton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor preferenceEditor;
    private Type type;
    private Gson gson = new Gson();
    private String json;
    public static final String EXTRA_KEY = "key";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //shared preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        preferenceEditor  = sharedPreferences.edit();

        if (!sharedPreferences.contains("MyArray")) {
            peopleList = new ArrayList<>();
            json =  gson.toJson(peopleList);
            preferenceEditor.putString("MyArray", json);
            preferenceEditor.commit();
        }
        else {
            //if already set, then just retrieve the array from the shared preferences
            type = new TypeToken<List<Person>>(){}.getType();
            json = sharedPreferences.getString("MyArray", "");
            if (gson.fromJson(json, type) == null) {
                peopleList = new ArrayList<>();
                //the things null
            }
            else {
                peopleList = gson.fromJson(json, type);
                //the things not null
            }
        }



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
        View addPersonView = getLayoutInflater().inflate(R.layout.pop_up_add_person, null);
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

                    //First, convert the EditText to String variables
                    String sName = etName.getText().toString();
                    String sAmountOwed = etAmountOwed.getText().toString();
                    String sDate = etDate.getText().toString();
                    String sDescription = etDescription.getText().toString();
                    int iAmountOwed = 0;

                    if (!sAmountOwed.equals("")) {
                        //Second, convert our amount owed to an integer
                        iAmountOwed = Integer.parseInt(sAmountOwed);
                    }
                    
                    if (!sDescription.equals("")) {
                        if (sDate.equals("")) {
                            Toast.makeText(MainActivity.this, "Please enter a date", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            peopleList.add(new Person(sName, sDescription, sDate, iAmountOwed));

                            json =  gson.toJson(peopleList);
                            preferenceEditor.putString("MyArray", json);
                            preferenceEditor.apply();
                        }
                    }
                    else {
                        if(!sDate.equals("")){
                            peopleList.add(new Person(sName));
                            json =  gson.toJson(peopleList);
                            preferenceEditor.putString("MyArray", json);
                            preferenceEditor.apply();
                        }
                        else {
                            peopleList.add(new Person(sName, sDate, iAmountOwed));

                            json =  gson.toJson(peopleList);
                            preferenceEditor.putString("MyArray", json);
                            preferenceEditor.apply();
                        }
                    }
                    dialog.dismiss();
                    sortByName();
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

    private void sortByName(){
        Collections.sort(peopleList);
        peopleAdapter.notifyDataSetChanged();
    }

    private void sortByOweAmount(){

    }
}


/*
Ideas
- Possibly make a fake Person object as the first item in the array list to use as an add Person button
- Would have to identify the position and the difference between this and others when writing intents to change screens

- To do only if there is extra time.
 */