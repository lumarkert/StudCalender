package com.example.lukas.studcalender;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Date;

public class DayActivity extends AppCompatActivity {
    Kalender kalender;
    Date currentDate;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        Intent intent = getIntent();
        kalender = (Kalender) intent.getSerializableExtra("kalendar");
        currentDate = (Date) intent.getSerializableExtra("date");

    }
}
