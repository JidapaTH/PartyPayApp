package com.example.admin.partypay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Admin on 8/29/2017.
 */

public class SettingActivity extends AppCompatActivity {
    private final int LAN_EN = 0;
    private final int LAN_TH = 1;
    private final int CUR_EN = 0;
    private final int CUR_TH = 1;

    private int language = LAN_EN;
    private int currency = CUR_EN;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //TODO: Link to backend
        final TextView lanEN = (TextView) findViewById(R.id.language_en);
        final TextView lanTH = (TextView) findViewById(R.id.language_th);
        final TextView curEN = (TextView) findViewById(R.id.currency_en);
        final TextView curTH = (TextView) findViewById(R.id.currency_th);

        lanEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language = LAN_EN;
                setView(lanEN,lanTH);
            }
        });
        lanTH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language = LAN_TH;
                setView(lanTH,lanEN);
            }
        });
        curTH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currency = CUR_TH;
                setView(curTH,curEN);
            }
        });
        curEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currency = CUR_EN;
                setView(curEN,curTH);
            }
        });
    }

    private void setView(TextView mainTextView,TextView secondTextView){
        mainTextView.setTextColor(ContextCompat.getColor(SettingActivity.this,R.color.black));
        mainTextView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.default_border, null));
        secondTextView.setTextColor(ContextCompat.getColor(SettingActivity.this,R.color.grey));
        secondTextView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.grey_default_border, null));
    }
}
