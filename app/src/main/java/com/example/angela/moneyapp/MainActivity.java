package com.example.angela.moneyapp;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
        peopleListView = (ListView) findViewById(R.id.listView_people);
        addPersonButton = (Button) findViewById(R.id.button_add_person);
    }

    private void addPerson() {
        //startActivity(new Intent(MainActivity.this, PopUpAddPerson.class));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_add_person:
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.pop_up_add_person, null);
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
