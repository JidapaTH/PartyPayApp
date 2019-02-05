package com.example.admin.partypay.TransactionFile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.admin.partypay.R;
import com.example.admin.partypay.db.Overview;
import com.example.admin.partypay.db.TranHelper;

import java.util.ArrayList;

/**
 * Created by Admin on 8/29/2017.
 */

public class EditTransactionActivity extends AppCompatActivity {
    private int transID = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);
        Bundle bundle = getIntent().getExtras();
        if (bundle!= null) {
            transID = bundle.getInt("EventCategory");
        }

        TranHelper th = new TranHelper(EditTransactionActivity.this);
        Overview ov = th.searchTrans(transID);
    }
}
