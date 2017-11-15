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

    private void viewOwe(final int pos) {
        AlertDialog.Builder viewOweBuilder = new AlertDialog.Builder(PersonActivity.this);
        View viewOweView = getLayoutInflater().inflate(R.layout.pop_up_view_owe, null);
        final AlertDialog dialog = viewOweBuilder.create();

         final Owe currentOwe = oweList.get(pos);


         //TODO This doesn't seem to exist?
        //Wiring the dialog widgets
        TextView oweDateTextView = (TextView) findViewById(R.id.textView_owe_date);
        TextView amountOwedTextView = (TextView) findViewById(R.id.textView_pay_amount_owed);
        final TextView amountPaidTextView = (TextView) findViewById(R.id.textView_amount_paid);
        final EditText amountToPayEditText = (EditText) findViewById(R.id.editText_pay_owe);
        Button payAmountButton = (Button) findViewById(R.id.button_pay);
        Button donePayButton = (Button) findViewById(R.id.button_pay_done);

        //Setting the textview text
        oweDateTextView.setText(currentOwe.getDate());
        amountOwedTextView.setText(currentOwe.getAmount());
        amountPaidTextView.setText(currentOwe.getAmountPaid());

        if(currentOwe.isPaid()) {
            amountToPayEditText.setActivated(false); ;
            amountToPayEditText.setText("Paid");
        }

        /**
         * When submitted, we'll convert the data of our widget variables to be applicable for the 'Person' class
         * The dialog will not accept an empty name field, all other fields are optional
         */
        payAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!amountToPayEditText.getText().toString().isEmpty()) {
                    int amountToPay = Integer.parseInt(amountToPayEditText.getText().toString());
                    if(currentOwe.getAmount() < amountToPay + currentOwe.getAmountPaid()){

                        Toast.makeText(PersonActivity.this, "Entered value too large", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(PersonActivity.this, "Paid " + amountToPay, Toast.LENGTH_SHORT).show();
                        currentOwe.setAmountPaid(currentOwe.getAmount() + amountToPay);
                        oweList.remove(pos);
                        oweList.add(pos, currentOwe);

                        json =  gson.toJson(oweList);
                        preferenceEditor.putString("MyOweArray", json);
                        preferenceEditor.apply();

                        amountPaidTextView.setText(currentOwe.getAmountPaid());

                        dialog.dismiss();
                    }
                }
                else {
                    Toast.makeText(PersonActivity.this, "Please enter amount to pay", Toast.LENGTH_SHORT).show();
                }
            }
        });
//
        donePayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setView(viewOweView);
        dialog.show();
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
