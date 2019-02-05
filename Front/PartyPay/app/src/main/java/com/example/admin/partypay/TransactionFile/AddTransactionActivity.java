package com.example.admin.partypay.TransactionFile;

import android.animation.LayoutTransition;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.partypay.CustomSpinnerAdapter;
import com.example.admin.partypay.EventActivityFile.EventActivity;
import com.example.admin.partypay.ExpandableHeightGridView;
import com.example.admin.partypay.MainActivityFile.Event;
import com.example.admin.partypay.R;
import com.example.admin.partypay.db.EventHelper;
import com.example.admin.partypay.db.TranHelper;
import com.example.admin.partypay.MemberFile.Member;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.joda.time.LocalDateTime;

import static android.media.CamcorderProfile.get;


public class AddTransactionActivity extends AppCompatActivity {

    private static final String STATE_LOCAL_DATE_TIME = "state.local.date.time";
    //Spinner parameters for transaction type
    //private Spinner mTypeSpinner;
    private final int TYPE_NONE = -1;
    private final int TYPE_PAYMENT = 0;
    private final int TYPE_RECEIVE = 1;
    private final int TYPE_TRANSFER = 2;
    // Spinner parameters for payer
    private final int INVOLVE_NONE = -1;
    private final int INVOLVE_ALL = 0;
    private final int INVOLVE_ALL_EXCEPT = 1;
    private final int INVOLVE_ONLY = 2;
    //private int mOriginalTypeValue = TYPE_NONE;
    ScrollView parentView;
    int pInvolveValue = INVOLVE_NONE;
    ArrayList<PayerList> payersList = new ArrayList<>();
    ExpandableHeightGridView pInvolveGridView; // Need to be declared global for setupInvolveSpinner
    private int eventID;
    private ArrayList<Member> allMember = new ArrayList<>();
    private ArrayList<InvolveList> allMemberInvolve = new ArrayList<>();
    private int mTypeValue = TYPE_NONE;
    private Spinner mExpenseSpinner;
    private Spinner pInvolveSpinner;
    // Spinner parameters for transfer
    private Spinner tFromSpinner;
    private Spinner tToSpinner;
    private LocalDateTime mLocalDateTime = new LocalDateTime();

    //Animation to make transition more smooth
    private static void appear(Context ctx, LinearLayout v,int time){
        Animation a = AnimationUtils.loadAnimation(ctx,R.anim.appear_trans);
        a.setStartOffset(time);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    private static void disappear(Context ctx, LinearLayout v){
        Animation a = AnimationUtils.loadAnimation(ctx,R.anim.disappear_trans);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLocalDateTime = (LocalDateTime) savedInstanceState.getSerializable(STATE_LOCAL_DATE_TIME);
        }
        setContentView(R.layout.activity_add_transaction);
        parentView = (ScrollView) findViewById(R.id.add_transaction);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Retrieve necessary data from previous activity
            eventID = bundle.getInt("EventID");
        }

        //Import all members
        final EventHelper db = new EventHelper(this);
        Event event = db.getEvent(eventID);
        allMember = event.getEvent_member();
        for (int i = 0; i < allMember.size(); i++) {
//            allMemberName.add(allMember.get(i).get_name());
            allMemberInvolve.add(new InvolveList(allMember.get(i).get_id(), allMember.get(i).get_name()));
        }

        // Choose Transaction type (Payment/Receive/Transfer)
        final LinearLayout choice = (LinearLayout) findViewById(R.id.transaction_choice);
        final LinearLayout info = (LinearLayout) findViewById(R.id.transaction_info);
        ImageView choicePay = (ImageView) findViewById(R.id.choice_payment);
        ImageView choiceRec = (ImageView) findViewById(R.id.choice_receive);
        ImageView choiceTrans = (ImageView) findViewById(R.id.choice_transfer);
        final TextView choiceTextView = (TextView) findViewById(R.id.transaction_type_textview);
        final TextView expenseTextView = (TextView) findViewById(R.id.expense_text);
        final LinearLayout payReceiveSet = (LinearLayout) findViewById(R.id.payment_receive_set);
        final LinearLayout transferSet = (LinearLayout) findViewById(R.id.transfer_set);

        // Setup DateTimePicker
        final TextView pDateText = (TextView) findViewById(R.id.date_selection_payment);
        updateDateTimeTextView(pDateText);
        ImageView pDateEdit = (ImageView) findViewById(R.id.date_edit_payment);
        pDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                // If there is already a Date displayed, use that.
                Date dateToUse = (mLocalDateTime == null) ? new LocalDateTime().toDate() : mLocalDateTime.toDate();
                DateTimePickerFragment datePickerFragment =
                        FragmentFactory.createDatePickerFragment(dateToUse, "The", DateTimePickerFragment.BOTH,
                                new DateTimePickerFragment.ResultHandler() {
                                    @Override
                                    public void setDate(Date result) {
                                        mLocalDateTime = new LocalDateTime(result.getTime());
                                        updateDateTimeTextView(pDateText);
                                    }
                                });
                datePickerFragment.show(fragmentManager, DateTimePickerFragment.DIALOG_TAG);
            }
        });
        Log.v("AddTransationActivity","Value is :"+mTypeValue);
        //Setup VIEWS
        choicePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Setup text
                mTypeValue = TYPE_PAYMENT;
                choiceTextView.setText("Payment");
                expenseTextView.setText("Expense");

                //Setup views
                setupTransName();
                setupGridView();
                setupInvolveView();

                payReceiveSet.setVisibility(View.VISIBLE);
                transferSet.setVisibility(View.GONE);

                animateInfoView(info, choice);
                Log.v("AddTransactionActivity","Pay Clicked");

            }
        });
        choiceRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Setup text
                mTypeValue = TYPE_RECEIVE;
                choiceTextView.setText("Receive");
                expenseTextView.setText("Income");

                //Setup views
                setupTransName();
                setupGridView();
                setupInvolveView();

                payReceiveSet.setVisibility(View.VISIBLE);
                transferSet.setVisibility(View.GONE);

                animateInfoView(info, choice);

                Log.v("AddTransactionActivity","Receive Clicked");
            }
        });
        choiceTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Setup text
                mTypeValue = TYPE_TRANSFER;
                choiceTextView.setText("Transfer");
                expenseTextView.setText("Purpose");

                //Setup views
                setupTransName();
                setupTransferView();

                payReceiveSet.setVisibility(View.GONE);
                transferSet.setVisibility(View.VISIBLE);

                animateInfoView(info, choice);
                Log.v("AddTransactionActivity","Transfer Clicked");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_member, menu);
        return true;
    }

    // Actions when user click on save button
    // If there's no event name or error for adding data to database, show Toast
    // Otherwise, add event to database & go to EventActivity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                boolean intentAccessed = true;
                String date = "";
                String expense = "";
                List<Double> amount = new ArrayList<Double>();
                ArrayList<Integer> payersMember = new ArrayList<>();
                ArrayList<Integer> involveMember = new ArrayList<>();
                String payerMemo = "";
                String errorMessage = "Error creating transaction";

                //Check if any information is invalid
                if(mTypeValue == TYPE_NONE){
                    intentAccessed = false;
                    errorMessage = "Transfer: Select transaction type";
                } else {
                    date = formatDateStringOutput(mLocalDateTime);

                    if (mExpenseSpinner.getSelectedItemPosition() == mExpenseSpinner.getCount() - 1) {
                        EditText otherExpense = (EditText) findViewById(R.id.payment_other_edittext);
                        expense = otherExpense.getText().toString();
                    } else {
                        expense = mExpenseSpinner.getSelectedItem().toString();
                    }
                    if (expense.trim().length() == 0) {
                        intentAccessed = false;
                        switch (mTypeValue) {
                            case 0:
                                errorMessage = "Payment: Type expense name";
                                break;
                            case 1:
                                errorMessage = "Receive: Type income name";
                                break;
                            case 2:
                                errorMessage = "Transfer: Type option name";
                                break;
                        }
                    } else {
                        EditText payerMemoEditText = (EditText) findViewById(R.id.memo_edittext_payment);
                        payerMemo = payerMemoEditText.getText().toString();
                    }
                    if (mTypeValue == TYPE_PAYMENT || mTypeValue == TYPE_RECEIVE) {
                        for (int i = 0; i < payersList.size(); i++) {
                            // Get only those with amount > 0
                            if (payersList.get(i).getAmount() > 0) {
                                payersMember.add(payersList.get(i).getMemberName());
                                amount.add(payersList.get(i).getAmount());
                            }
                        }
                        if (payersMember.isEmpty()) {
                            intentAccessed = false;
                            errorMessage = "'Amount' must be more than 0";
                        }
                        if (pInvolveValue == INVOLVE_ALL) {
                            for (int i = 0; i < allMemberInvolve.size(); i++) {
                                involveMember.add(allMemberInvolve.get(i).getID());
                            }
                        } else if (pInvolveValue == INVOLVE_ALL_EXCEPT) {
                            for (int i = 0; i < allMemberInvolve.size(); i++) {
                                if (!allMemberInvolve.get(i).getChecked()) {
                                    involveMember.add(allMemberInvolve.get(i).getID());
                                }
                            }
                        } else {
                            for (int i = 0; i < allMemberInvolve.size(); i++) {
                                if (allMemberInvolve.get(i).getChecked()) {
                                    involveMember.add(allMemberInvolve.get(i).getID());
                                }
                            }
                        }
                        if (involveMember.size() == 0) {
                            intentAccessed = false;
                            errorMessage = "Select people involve";
                        }
                    } else {
                        payersMember.add(allMemberInvolve.get(tFromSpinner.getSelectedItemPosition()).getID());
                        involveMember.add(allMemberInvolve.get(tToSpinner.getSelectedItemPosition()).getID());

                        if (Objects.equals(payersMember.get(0), involveMember.get(0))) {
                            intentAccessed = false;
                            errorMessage = "Transfer: Transfer members must be different";
                        }
                        EditText transferAmount = (EditText) findViewById(R.id.transfer_amount_edittext);
                        String transAmount = transferAmount.getText().toString();
                        if (transAmount.isEmpty() || transAmount.equals(".")) {
                            //                        amount.add(0.0);
                            intentAccessed = false;
                            errorMessage = "Transfer: Please type amount of money ";
                        } else {
                            if (Double.parseDouble(transAmount) <= 0) {
                                intentAccessed = false;
                                errorMessage = "Transfer: Transfer money must be more than zero";
                            } else {
                                amount.add(Double.parseDouble(transAmount));
                            }
                        }
                    }
                }
                if (intentAccessed) {
                    TranHelper th = new TranHelper(this);
                    if (mTypeValue == TYPE_PAYMENT) {
                        th.add_payment(expense, eventID, payersMember, amount, involveMember, date, payerMemo);
                    } else if (mTypeValue == TYPE_RECEIVE) {
                        th.add_recieve(expense, eventID, payersMember, amount, involveMember, date, payerMemo);
                    } else {
                        th.add_tranfer(expense, eventID, amount.get(0), payersMember.get(0), involveMember.get(0), date, payerMemo);
                    }
                    //Log for recheck
                    Log.i("AddTransactionActivity", "Name: " + expense);
                    Log.i("AddTransactionActivity", "ID: " + eventID);
                    for (int i = 0; i < payersMember.size(); i++) {
                        Log.i("AddTransactionActivity", "payersMember#" + i + ": " + payersMember.get(i));
                        Log.i("AddTransactionActivity", "amount#" + i + ": " + amount.get(i));
                    }
                    for (int i = 0; i < involveMember.size(); i++) {
                        Log.i("AddTransactionActivity", "involveMember#" + i + ": " + involveMember.get(i));
                    }
                    Log.i("AddTransactionActivity", "Date: " + date);

                    Intent intent = new Intent(AddTransactionActivity.this, EventActivity.class);
                    intent.putExtra("EventID", eventID);
                    startActivity(intent);
                    finish();
                } else {
                    Toast toast = Toast.makeText(AddTransactionActivity.this, errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddTransactionActivity.this, EventActivity.class);
        intent.putExtra("EventID",eventID);
        startActivity(intent);
        finish();
    }

    private void animateInfoView(final LinearLayout info,final LinearLayout choice){
        disappear(AddTransactionActivity.this,choice);
        choice.setVisibility(View.GONE);

        appear(AddTransactionActivity.this,info,400);
        info.setVisibility(View.VISIBLE);
    }

    private void setupTransName(){
        mExpenseSpinner = (Spinner) findViewById(R.id.expense_spinner);
        ArrayList<String> expenseOptions = new ArrayList<>();
        switch(mTypeValue){
            case TYPE_PAYMENT: expenseOptions.add("Food & Drinks");expenseOptions.add("Petrol");
                expenseOptions.add("Hotel");expenseOptions.add("Others");break;
            case TYPE_RECEIVE: expenseOptions.add("Salary");expenseOptions.add("Reimburstment");
                expenseOptions.add("Donation");expenseOptions.add("Others");break;
            case TYPE_TRANSFER: expenseOptions.add("Borrowing");expenseOptions.add("Repayment");expenseOptions.add("Others"); break;
        }

        CustomSpinnerAdapter expenseAdapter = new CustomSpinnerAdapter(expenseOptions, AddTransactionActivity.this);
        mExpenseSpinner.setAdapter(expenseAdapter);
        final EditText otherExpense = (EditText) findViewById(R.id.payment_other_edittext);
        mExpenseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == mExpenseSpinner.getCount() - 1) {
                    otherExpense.setVisibility(View.VISIBLE);
                } else {
                    otherExpense.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupGridView(){
        //Create GridView
        final ExpandableHeightGridView pGridView = (ExpandableHeightGridView) findViewById(R.id.payment_gridview);
        final LayoutTransition pTransition = pGridView.getLayoutTransition();
        pTransition.disableTransitionType(LayoutTransition.APPEARING);
        pGridView.setExpanded(true);
        payersList.add(new PayerList(0)); // Create first payer
        Log.v("AddTransaction","PayersList"+payersList);
        Log.v("AddTransaction","AllMember"+allMember);
        Log.v("AddTransaction","mTypeValue"+mTypeValue);
        final PayerListAdapter pGridViewAdapter = new PayerListAdapter(AddTransactionActivity.this, payersList, allMember, mTypeValue);
        pGridView.setVerticalScrollBarEnabled(false);
        pGridView.setAdapter(pGridViewAdapter);
        pGridViewAdapter.notifyDataSetChanged();

        final Button addPayer = (Button) findViewById(R.id.add_payer_button);
        if(mTypeValue == TYPE_PAYMENT){
            addPayer.setText("Add Payer");
        }else {
            addPayer.setText("Add Receiver");
        }

        addPayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pTransition.enableTransitionType(LayoutTransition.APPEARING);
                ArrayList<PayerList> memberSet = new ArrayList<>();
                memberSet.add(new PayerList(0)); // Create extra payer
                pGridViewAdapter.add(memberSet.get(0));
                pGridViewAdapter.notifyDataSetChanged();

                //TODO: Cannot scroll because of involve list (Next patch maybe?)
                    /*
                    LinearLayout l = (LinearLayout) findViewById(R.id.payer_line);
                    l.requestFocus();
                    int valueInPixels = (int) getResources().getDimension(R.dimen.payer_height);
                    scrollDownBy(parentView,valueInPixels);*/
            }
        });
    }

    private void setupInvolveView(){
        //Setup involve views (All, All except, Only)
        pInvolveSpinner = (Spinner) findViewById(R.id.involve_spinner_payment);
        setupInvolveSpinner();

        //GridView to choose member involved
        pInvolveGridView = (ExpandableHeightGridView) findViewById(R.id.involve_gridview_payment);
        pInvolveGridView.setExpanded(true);
        final InvolveListAdapter involveListAdapter = new InvolveListAdapter(this, allMemberInvolve);
        pInvolveGridView.setAdapter(involveListAdapter);
    }

    private void setupTransferView(){
        //Setup from & to & amount
        //Spinner to choose member in 'From' list
        tFromSpinner = (Spinner) findViewById(R.id.from_transfer_spinner);
        final CustomSpinnerAdapter fromAdapter = new CustomSpinnerAdapter(AddTransactionActivity.this, allMember);
        tFromSpinner.setAdapter(fromAdapter);
        //Spinner to choose member in 'To' list
        tToSpinner = (Spinner) findViewById(R.id.to_transfer_spinner);
        final CustomSpinnerAdapter toAdapter = new CustomSpinnerAdapter(AddTransactionActivity.this, allMember);
        tToSpinner.setAdapter(toAdapter);
    }

    private void setupInvolveSpinner() {
        /**  Create adapter for spinner. The list options are from the String array it will use
         *   the spinner will use the default layout
         */
        ArrayAdapter typeSpinnerAdapter = ArrayAdapter.createFromResource(AddTransactionActivity.this,
                R.array.array_involve_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        pInvolveSpinner.setAdapter(typeSpinnerAdapter);
        pInvolveSpinner.setSelection(pInvolveValue);

        // Set the integer mSelected to the constant values
        pInvolveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.transaction_involve_0))) {
                        pInvolveValue = INVOLVE_ALL; // All
                    } else if (selection.equals(getString(R.string.transaction_involve_1))){
                        pInvolveValue = INVOLVE_ALL_EXCEPT; // All except someone
                    } else if(selection.equals(getString(R.string.transaction_involve_2))){
                        pInvolveValue = INVOLVE_ONLY; // Only someone involve
                    } else {
                        pInvolveValue = INVOLVE_NONE; // No selection
                    }
                    Log.v("AddTransactionActivity","Value:"+ pInvolveValue);
                    if(pInvolveValue == INVOLVE_ALL_EXCEPT || pInvolveValue == INVOLVE_ONLY ){
                        pInvolveGridView.setVisibility(View.VISIBLE);
                    } else {
                        pInvolveGridView.setVisibility(View.GONE);
                    }
                }

            }
            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pInvolveValue = INVOLVE_ALL; // Default as no selection
            }
        });
    }

    private void focusOnView(final ScrollView scroll, final View view) {
        Log.i("AddTransaction","focusOnView triggered: "+view.getTop());
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.smoothScrollTo(0, view.getTop());
            }
        });
    }

    private void scrollDownBy(final ScrollView scroll, final View view) {
        Log.i("AddTransaction","scrollDownView triggered: "+view.getMeasuredHeight());
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.smoothScrollBy(0, view.getMeasuredHeight());
            }
        });
    }

    private void scrollDownBy(final ScrollView scroll, final int dp) {
        Log.i("AddTransaction","scrollDownView triggered: "+dp2px(dp));
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.smoothScrollBy(0, dp2px(dp));
            }
        });
    }

    //Use this to change dp unit to pixel unit
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private String formatDateString(LocalDateTime localDateTime) {
        return localDateTime.toString("dd/MM/yyyy HH:mm");
    }

    private String formatDateStringOutput(LocalDateTime localDateTime) {
        return localDateTime.toString("MM/dd/yyyy'T'HH:mm:ss");
    }
    
    private void updateDateTimeTextView(TextView textView) {
        textView.setText(formatDateString(mLocalDateTime));
    }
}