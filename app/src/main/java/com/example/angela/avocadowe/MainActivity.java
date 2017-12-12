package com.example.angela.avocadowe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
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
    private FloatingActionButton addPersonFloatingActionButton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor preferenceEditor;
    private Type type;
    private Gson gson = new Gson();
    private String json;
    public static final String EXTRA_KEY = "key";
    public static final String EXTRA_ARRAY_KEY = "array key";
    public static final String POS_KEY = "position key";


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

        Intent get = getIntent();
        if (get.getParcelableArrayListExtra(PersonActivity.SEND_KEY) != null) {

            Toast.makeText(this, "It is not null", Toast.LENGTH_SHORT).show();
            peopleList = get.getParcelableArrayListExtra(PersonActivity.SEND_KEY);
            json =  gson.toJson(peopleList);
            preferenceEditor.putString("MyArray", json);
            preferenceEditor.apply();
        }
        else {
            Toast.makeText(this, "It is null", Toast.LENGTH_SHORT).show();
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
                send.putExtra(EXTRA_ARRAY_KEY, peopleList);
                send.putExtra(POS_KEY, pos);
                startActivity(send);
            }
        });
        peopleListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
                showMenu(view, pos);
                return true;
            }
        });
    }

    private void showMenu(View view, final int pos){
        PopupMenu deleteMenu = new PopupMenu(this, view);
        deleteMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.pop_up_item_delete:
                        peopleList.remove(pos);
                        json = gson.toJson(peopleList);
                        preferenceEditor.putString("MyArray", json);
                        preferenceEditor.apply();
                        sortByName();
                        break;
                }
                return true;
            }
        });
        deleteMenu.inflate(R.menu.pop_up_menu);
        deleteMenu.show();
    }


    private void setOnClickListeners() {addPersonFloatingActionButton.setOnClickListener(this);}

    private void wiringWidgets() {
        //background = (ConstraintLayout) findViewById(R.id.layout_main_background);
        //background.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        peopleListView = findViewById(R.id.listView_people);
        addPersonFloatingActionButton = findViewById(R.id.floatingActionButton_add_person);
    }

    private void addPerson() {
        AlertDialog.Builder addPersonBuilder = new AlertDialog.Builder(MainActivity.this);
        View addPersonView = getLayoutInflater().inflate(R.layout.pop_up_add_person, null);
        final AlertDialog dialog = addPersonBuilder.create();

        //Wiring the dialog widgets
        final EditText etName = addPersonView.findViewById(R.id.editText_pop_up_add_name);
        final EditText etAmountOwed = addPersonView.findViewById(R.id.editText_pop_up_add_person_amount_owed);
        final EditText etDate = addPersonView.findViewById(R.id.editText_pop_up_add_person_date);
        final EditText etDescription = addPersonView.findViewById(R.id.editText_pop_up_add_person_description);
        Button submit = addPersonView.findViewById(R.id.button_pop_up_add_person_submit);
        Button cancel = addPersonView.findViewById(R.id.button_pop_up_add_person_cancel);

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

                    if (!sAmountOwed.equals("")) { //if there is an amount, then require a date
                        //Second, convert our amount owed to an integer
                        iAmountOwed = Integer.parseInt(sAmountOwed);
                        if (sDate.equals("")) {
                            Toast.makeText(MainActivity.this, "Please enter a date", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            peopleList.add(new Person(sName, sDescription, sDate, iAmountOwed));

                            json =  gson.toJson(peopleList);
                            preferenceEditor.putString("MyArray", json);
                            preferenceEditor.apply();

                            dialog.dismiss();
                            sortByName();
                        }
                    }
                    else { //if there is no amount
                        peopleList.add(new Person(sName));

                        json = gson.toJson(peopleList);
                        preferenceEditor.putString("MyArray", json);
                        preferenceEditor.apply();

                        dialog.dismiss();
                        sortByName();
                    }
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
            case R.id.floatingActionButton_add_person:
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
- Would have to identify the position and the difference between this and others when writing intents to change screens

- To do only if there is extra time.
 */

/*

TODO: have the textview for each listview item change color to show whether it was paid or not. Update the array for whether or not the thing was paid and the amount paid

TODO transfer the extra pay to the next owe?

 Change person name font
 change button colors for accent

 change button styles
 change font size for view owe

 format textview list item size
 pictures?


 */