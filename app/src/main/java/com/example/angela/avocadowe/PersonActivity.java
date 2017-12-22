package com.example.angela.avocadowe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PersonActivity extends AppCompatActivity implements View.OnClickListener {

    private String personName;
    private TextView nameTextView;
    private ListView oweListView;
    private ArrayList<Owe> oweList;
    private ArrayAdapter<Owe> oweAdapter;
    private Person currentPerson;
    private ImageButton helpPageButton;
    private FloatingActionButton addAmountFloatingActionButton;
    private ArrayList<Person> personList;
    private int currentPersonPos, amountToPay;
    public static final String SEND_KEY = "key2";
    public static final String PAYPAL_CLIENT_ID = "AWDifm2bTvFUvwMR_A5z_C335nOmAfz9yUERxZEytDtNjXtfCKPG6zi8x582wMIzQD5LYufOeXDR49a8"; //Placeholder client
    public static final int PAYPAL_REQUEST_CODE = 123; //PayPal request code for onActivityResult method
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId(PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        wiringWidgets();
        setOnClickListeners();

        Intent get = getIntent();
        currentPerson = get.getParcelableExtra(MainActivity.EXTRA_KEY);
        currentPersonPos = get.getIntExtra(MainActivity.POS_KEY, 0);
        personName = currentPerson.getName();
        nameTextView.setText(personName);
        personList = get.getParcelableArrayListExtra(MainActivity.EXTRA_ARRAY_KEY);

        oweList = currentPerson.getOweList();

        adaptArray();

        //start PayPal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }

    private void setOnClickListeners() {
        addAmountFloatingActionButton.setOnClickListener(this);
        helpPageButton.setOnClickListener(this);
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
        oweListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                switch (id) {
                    case R.id.pop_up_item_delete:
                        oweList.remove(pos);
                        currentPerson.setOweList(oweList);
                        personList.remove(currentPersonPos);
                        personList.add(currentPersonPos, currentPerson);
                        sort();
                        break;
                }
                return false;
            }
        });
        deleteMenu.inflate(R.menu.pop_up_menu);
        deleteMenu.show();
    }

    private void sort() {
        oweAdapter.notifyDataSetChanged();
    }

    private void viewOwe(final int pos) {
        AlertDialog.Builder viewOweBuilder = new AlertDialog.Builder(PersonActivity.this);
        View viewOweView = getLayoutInflater().inflate(R.layout.pop_up_view_owe, null);
        final AlertDialog dialog = viewOweBuilder.create();

        final Owe currentOwe = oweList.get(pos);

        //Wiring the dialog widgets
        TextView oweDateTextView = viewOweView.findViewById(R.id.textView_owe_date);
        TextView amountOwedTextView = viewOweView.findViewById(R.id.textView_pay_amount_owed);
        TextView descriptionTextView = viewOweView.findViewById(R.id.textView_description);
        final TextView amountPaidTextView = viewOweView.findViewById(R.id.textView_amount_paid);
        final EditText amountToPayEditText = viewOweView.findViewById(R.id.editText_pay_owe);
        Button pay = viewOweView.findViewById(R.id.button_pay);
        Button done = viewOweView.findViewById(R.id.button_pay_done);

            //Setting the textview text
        descriptionTextView.setText("" + currentOwe.getDescription());
        oweDateTextView.setText(currentOwe.getDate() + " - $" + currentOwe.getAmount());
        amountOwedTextView.setText("Amount Still Owed: $" + (currentOwe.getAmount() - currentOwe.getAmountPaid()));
        amountPaidTextView.setText("Amount Paid: $" + currentOwe.getAmountPaid());
        Log.d("TAG", "viewOwe: " + currentOwe.getAmountPaid());

        if(currentOwe.isPaid()) {
            amountToPayEditText.setEnabled(false);
            pay.setEnabled(false);
            amountToPayEditText.setHint("Paid");
        }

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!amountToPayEditText.getText().toString().isEmpty()) {
                    amountToPay = Integer.parseInt(amountToPayEditText.getText().toString());
                    if(currentOwe.getAmount() < amountToPay + currentOwe.getAmountPaid()){

                        Toast.makeText(PersonActivity.this, "Entered value too large", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        if(currentOwe.getAmountPaid() + amountToPay == currentOwe.getAmount()) {
                            Toast.makeText(PersonActivity.this, "Paid!", Toast.LENGTH_SHORT).show();


                            Log.d("TAG", "viewOwe: " + currentOwe.getAmountPaid());

                            currentOwe.setAmountPaid(currentOwe.getAmount());
                            currentOwe.setPaid(true);
                            oweList.remove(pos);
                            oweList.add(pos, currentOwe);
                            currentPerson.setOweList(oweList);
                            personList.remove(currentPersonPos);
                            personList.add(currentPersonPos, currentPerson);
                            sort();

                            Log.d("TAG", "viewOwe: " + currentOwe.getAmountPaid());
                        }
                        else{
                            Toast.makeText(PersonActivity.this, "Paid " + amountToPay, Toast.LENGTH_SHORT).show();


                            Log.d("TAG", "viewOwe: " + currentOwe.getAmountPaid());

                            currentOwe.setAmountPaid(currentOwe.getAmountPaid() + amountToPay);
                            oweList.remove(pos);
                            oweList.add(pos, currentOwe);
                            currentPerson.setOweList(oweList);
                            personList.remove(currentPersonPos);
                            personList.add(currentPersonPos, currentPerson);
                            sort();

                            Log.d("TAG", "viewOwe: " + currentOwe.getAmountPaid());
                        }
                        getPayment();

                        amountPaidTextView.setText("" + currentOwe.getAmountPaid());

                        dialog.dismiss();
                    }
                }
                else {
                    Toast.makeText(PersonActivity.this, "Please enter amount to pay", Toast.LENGTH_SHORT).show();
                }
            }
        });
//
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setView(viewOweView);
        dialog.show();
    }

    private void getPayment() { //new intent configuration to start PayPal
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(amountToPay)), "USD", "AvocadOwe Transferification",
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE); //invokes onActivityResult
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from PayPal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private void wiringWidgets() {
        nameTextView = findViewById(R.id.textView_name);
        oweListView = findViewById(R.id.listView_owes);
        addAmountFloatingActionButton = findViewById(R.id.floatingActionButton_new_amount);
        helpPageButton = findViewById(R.id.imageButton_help2);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.floatingActionButton_new_amount:
                addAmount();
                break;
            case R.id.imageButton_help2:
                helpPage();
                break;
            default:
                break;
        }
    }

    public void onBackPressed() {
        Intent sendBack = new Intent(PersonActivity.this, MainActivity.class);
        sendBack.putExtra(SEND_KEY, personList);
        startActivity(sendBack);
        super.onBackPressed();
    }

    private void addAmount() {
        AlertDialog.Builder addAmountBuilder = new AlertDialog.Builder(PersonActivity.this);
        View addAmountView = getLayoutInflater().inflate(R.layout.pop_up_add_amount, null);
        final AlertDialog dialog = addAmountBuilder.create();

        //Wiring the dialog widgets
        final EditText etAmountOwed = addAmountView.findViewById(R.id.editText_pop_up_add_amount_amount_owed);
        final EditText etDate = addAmountView.findViewById(R.id.editText_pop_up_add_amount_date);
        final EditText etDescription = addAmountView.findViewById(R.id.editText_pop_up_add_amount_description);
        Button submit = addAmountView.findViewById(R.id.button_pop_up_add_amount_submit);
        Button cancel = addAmountView.findViewById(R.id.button_pop_up_add_amount_cancel);

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
                            currentPerson.setOweList(oweList);
                            personList.remove(currentPersonPos);
                            personList.add(currentPersonPos, currentPerson);
                            sort();

                            dialog.dismiss();
                        }
                    }
                    else {
                        if(!sDate.equals("")){
                            oweList.add(new Owe(iAmountOwed, sDate));
                            currentPerson.setOweList(oweList);
                            personList.remove(currentPersonPos);
                            personList.add(currentPersonPos, currentPerson);
                            sort();

                            dialog.dismiss();
                        }
                        else {
                            Toast.makeText(PersonActivity.this, "Please enter a date", Toast.LENGTH_SHORT).show();
                        }
                    }
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

    private void helpPage() {
        AlertDialog.Builder helpPageBuilder = new AlertDialog.Builder(PersonActivity.this);
        View helpPageView = getLayoutInflater().inflate(R.layout.help_page, null);
        final AlertDialog dialog = helpPageBuilder.create();

        dialog.setView(helpPageView);
        dialog.show();
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class)); //destroy paypal service when closing the app
        super.onDestroy();
    }
}
